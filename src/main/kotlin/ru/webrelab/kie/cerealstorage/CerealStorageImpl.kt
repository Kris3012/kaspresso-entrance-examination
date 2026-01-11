package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество не может быть отрицательным" }
        val currentAmount = storage[cereal] ?: 0f
        val space = containerCapacity - currentAmount
        val addedAmount = if (amount <= space) amount else space
        val leftover = if (amount <= space) 0f else amount - space
        val maxSpace = (storageCapacity / containerCapacity).toInt()
        if (storage[cereal] == null && storage.size >= maxSpace) {
            throw IllegalStateException("Нет места для нового контейнера")
        }
        storage[cereal] = currentAmount + addedAmount
        return leftover
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество не может быть отрицательным" }
        val currentAmount = storage[cereal] ?: 0f
        val takenAmount = if (amount < currentAmount) amount else currentAmount
        storage[cereal] = currentAmount - takenAmount
        return takenAmount
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val container = storage[cereal]
        return when {
            container == null -> false
            container == 0f -> {
                storage.remove(cereal)
                true
            }

            container > 0f -> false
            else -> {
                false
            }
        }

    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        storage[cereal] ?: throw IllegalStateException("Контейнер не найден")
        val availableSpace = containerCapacity - getAmount(cereal)
        return availableSpace
    }

    override fun toString(): String =
        if (storage.isEmpty()) "Хранилище не должно быть пустым"
        else storage.entries.joinToString(", ") {
            "${it.key}: ${it.value}"
        }


    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

}

