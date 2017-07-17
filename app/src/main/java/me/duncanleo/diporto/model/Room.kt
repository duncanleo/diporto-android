package me.duncanleo.diporto.model

/**
 * Created by duncanleo on 17/7/17.
 */
data class Room (
        val id: Int,
        val name: String,
        val owner: User,
        val members: List<User>
)
