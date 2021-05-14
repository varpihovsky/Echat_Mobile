package com.example.echatmobile.model.db

import androidx.room.*
import com.example.echatmobile.model.db.entities.*
import com.example.echatmobile.model.db.entities.relations.InviteAndChat

@Dao
interface RoomDAO {

    /* Chat */

    @Insert
    fun insertChat(chat: Chat)

    @Delete
    fun deleteChat(chat: Chat)

    @Update
    fun updateChat(chat: Chat)

    @Query("SELECT * FROM Chat WHERE id = :chatId")
    fun getChatById(chatId: Long)

    /* Invite */

    @Insert
    fun insertInvite(invite: Invite)

    @Delete
    fun deleteInvite(invite: Invite)

    @Update
    fun updateInvite(invite: Invite)

    @Query("SELECT * FROM Invite")
    fun getAllInvites(): List<Invite>

    @Transaction
    @Query("SELECT * FROM Invite WHERE inviteId = :inviteId")
    fun getCompoundInvite(inviteId: Long): InviteAndChat

    @Transaction
    @Query("SELECT * FROM Invite")
    fun getAllCompoundInvites(): List<InviteAndChat>

    /* Message */

    @Insert
    fun insertMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)

    @Update
    fun updateMessage(message: Message)

    @Query("SELECT * FROM Message WHERE id = :chatId")
    fun getMessagesByChat(chatId: Long): List<Message>

    @Query("SELECT * FROM Message WHERE id = :chatId")
    fun getCompoundMessagesByChat(chatId: Long)

    /* User */

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUserById(userId: Long): User

    /* ChatAdminsCrossRef */

    @Insert
    fun insertChatAdminsCrossRef(chatAdminsCrossRef: ChatAdminsCrossRef)

    @Update
    fun updateChatAdminsCrossRef(chatAdminsCrossRef: ChatAdminsCrossRef)

    @Delete
    fun deleteChatAdminsCrossRef(chatAdminsCrossRef: ChatAdminsCrossRef)

    @Query("SELECT * FROM ChatAdminsCrossRef WHERE userId = :userId")
    fun getChatAdminsCrossRefsByUserId(userId: Long): List<ChatAdminsCrossRef>

    @Query("SELECT * FROM ChatAdminsCrossRef WHERE chatId = :chatId")
    fun getChatAdminsCrossRefsByChatId(chatId: Long): List<ChatAdminsCrossRef>

    /* ChatParticipantsCrossRef */

    @Insert
    fun insertChatParticipantsCrossRef(chatParticipantsCrossRef: ChatParticipantsCrossRef)

    @Update
    fun updateChatParticipantsCrossRef(chatParticipantsCrossRef: ChatParticipantsCrossRef)

    @Delete
    fun deleteChatParticipantsCrossRef(chatParticipantsCrossRef: ChatParticipantsCrossRef)

    @Query("SELECT * FROM ChatParticipantsCrossRef WHERE userId = :userId")
    fun getChatParticipantsCrossRefByUserId(userId: Long): List<ChatParticipantsCrossRef>

    @Query("SELECT * FROM ChatParticipantsCrossRef WHERE chatId = :chatId")
    fun getChatParticipantsCrossRefByChatId(chatId: Long): List<ChatParticipantsCrossRef>

    /* ReadHistory */

    @Insert
    fun insertReadHistory(readHistory: ReadHistory)

    @Update
    fun updateReadHistory(readHistory: ReadHistory)

    @Delete
    fun deleteReadHistory(readHistory: ReadHistory)

    @Query("SELECT * FROM ReadHistory WHERE messageId = :messageId")
    fun getReadHistoryByMessageId(messageId: Long): ReadHistory
}