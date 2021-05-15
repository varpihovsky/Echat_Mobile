package com.example.echatmobile.model.db

import com.example.echatmobile.model.db.entities.*
import com.example.echatmobile.model.db.entities.ReadHistory.Companion.NOT_READ
import com.example.echatmobile.model.db.entities.ReadHistory.Companion.READ
import com.example.echatmobile.model.entities.*

class EchatDatabase(private val roomDAO: RoomDAO) {
    fun addUser(user: DBConvertibleUser) {
        roomDAO.insertUser(user.toDBObj())
    }

    fun getUserById(id: Long): UserWithoutPassword? {
        return roomDAO.getUserById(id)?.let { UserWithoutPassword(it.id, it.login) }
    }

    fun addChat(chatDTO: ChatDTO) {
        roomDAO.insertChat(chatDTO.toDBObj())
        chatDTO.chatAdmins.forEach {
            roomDAO.insertChatAdminsCrossRef(ChatAdminsCrossRef(chatDTO.id, it.id))
            addUser(it)
        }
        chatDTO.chatParticipants.forEach {
            roomDAO.insertChatParticipantsCrossRef(ChatParticipantsCrossRef(chatDTO.id, it.id))
            addUser(it)
        }
    }

    fun getChatsByUserId(userId: Long): List<ChatDTO> {
        return roomDAO.getChatsByUserId(userId).map { mapChatToDTO(it) }
    }

    fun addMessage(messageDTO: MessageDTO) {
        roomDAO.insertMessage(messageDTO.toDBObj())
        addUser(messageDTO.sender)
        addReadHistory(messageDTO)
    }

    fun setMessageRead(messageId: Long) {
        roomDAO.getReadHistoryByMessageId(messageId)?.let {
            roomDAO.updateReadHistory(ReadHistory(it.id, it.messageId, READ))
        }
    }

    fun getMessagesByChat(chatId: Long) =
        roomDAO.getMessagesByChat(chatId).mapNotNull { mapMessageToDTO(it) }

    fun getNotReadMessages(): List<MessageDTO> =
        roomDAO.getNotReadMessages().mapNotNull { mapMessageToDTO(it) }

    fun addReadHistory(readHistory: ReadHistory) {
        roomDAO.insertReadHistory(readHistory)
    }

    private fun addReadHistory(messageDTO: MessageDTO) {
        roomDAO.insertReadHistory(ReadHistory(0, messageDTO.id, NOT_READ))
    }

    fun addInvite(inviteDTO: InviteDTO) {
        roomDAO.insertInvite(inviteDTO.toDBObj())
    }

    fun removeInviteById(inviteId: Long) {
        roomDAO.deleteInvite(Invite(inviteId, 0))
    }

    private fun mapChatToDTO(chat: Chat): ChatDTO =
        ChatDTO(
            chat.id,
            chat.name,
            roomDAO.getAdminsByChatId(chat.id)
                .map { user -> UserWithoutPassword(user.id, user.login) },
            roomDAO.getParticipantsByChatId(chat.id)
                .map { user -> UserWithoutPassword(user.id, user.login) },
        )

    private fun mapMessageToDTO(message: Message?): MessageDTO? {
        if (message == null) return null

        return MessageDTO(
            message.id,
            message.text,
            roomDAO.getUserById(message.senderId).let { UserWithoutPassword(it!!.id, it.login) },
            mapChatToDTO(roomDAO.getChatById(message.chatId)!!),
            mapMessageToDTO(roomDAO.getMessageById(message.questionId ?: -1)),
            message.created
        )
    }

}