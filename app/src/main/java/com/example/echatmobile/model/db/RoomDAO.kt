package com.example.echatmobile.model.db

import androidx.room.*
import com.example.echatmobile.model.db.entities.*
import com.example.echatmobile.model.db.entities.relations.InviteAndChat
import com.example.echatmobile.model.db.entities.relations.MessageAndMessageAndChatAndUser

@Dao
interface RoomDAO {

    /* Chat */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: Chat)

    @Delete
    fun deleteChat(chat: Chat)

    @Update
    fun updateChat(chat: Chat)

    @Query("SELECT * FROM Chat WHERE id = :chatId")
    fun getChatById(chatId: Long): Chat?

    @Query("SELECT * FROM Chat WHERE id = (SELECT chatId FROM ChatParticipantsCrossRef WHERE userId = :userId)")
    fun getChatsByUserId(userId: Long): List<Chat>

    /* Invite */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvite(invite: Invite)

    @Delete
    fun deleteInvite(invite: Invite)

    @Update
    fun updateInvite(invite: Invite)

    @Query("SELECT * FROM Invite")
    fun getAllInvites(): List<Invite>

    @Transaction
    @Query("SELECT * FROM Invite WHERE inviteId = :inviteId")
    fun getCompoundInvite(inviteId: Long): InviteAndChat?

    @Transaction
    @Query("SELECT * FROM Invite")
    fun getAllCompoundInvites(): List<InviteAndChat>

    /* Message */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)

    @Update
    fun updateMessage(message: Message)

    @Query("SELECT * FROM Message WHERE id = :id")
    fun getMessageById(id: Long): Message?

    @Query("SELECT * FROM Message WHERE chatId = :chatId")
    fun getMessagesByChat(chatId: Long): List<Message>

    @Transaction
    @Query("SELECT * FROM Message WHERE chatId = :chatId")
    fun getCompoundMessagesByChat(chatId: Long): List<MessageAndMessageAndChatAndUser>

    @Query("SELECT * FROM Message WHERE id = (SELECT chatId FROM ReadHistory WHERE status = '${ReadHistory.NOT_READ}')")
    fun getNotReadMessages(): List<Message>

    @Query("SELECT * FROM Message WHERE id = (SELECT chatId FROM ReadHistory WHERE status = '${ReadHistory.READ}')")
    fun getReadMessages(): List<Message>

    /* User */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUserById(userId: Long): User?

    @Query("SELECT * FROM User WHERE id = (SELECT userId FROM ChatParticipantsCrossRef WHERE chatId = :chatId)")
    fun getParticipantsByChatId(chatId: Long): List<User>

    @Query("SELECT * FROM User WHERE id = (SELECT userId FROM ChatAdminsCrossRef WHERE chatId = :chatId)")
    fun getAdminsByChatId(chatId: Long): List<User>

    /* ChatAdminsCrossRef */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReadHistory(readHistory: ReadHistory)

    @Update
    fun updateReadHistory(readHistory: ReadHistory)

    @Delete
    fun deleteReadHistory(readHistory: ReadHistory)

    @Query("SELECT * FROM ReadHistory WHERE messageId = :messageId")
    fun getReadHistoryByMessageId(messageId: Long): ReadHistory?
}