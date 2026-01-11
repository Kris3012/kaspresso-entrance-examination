package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 22f)

    //getAmount
    @Test
    fun `getAmount returns zero when container does not exist`() {
        val amount = storage.getAmount(Cereal.RICE)
        assertEquals(0f, amount)
    }

    @Test
    fun `getAmount returns storage amount if container is more than 0`() {
        storage.addCereal(Cereal.RICE, 3f)
        val amount = storage.getAmount(Cereal.RICE)
        assertEquals(3f, amount)
    }

    //removeContainer
    @Test
    fun `removeContainer return true if container is empty`() {
        storage.addCereal(Cereal.PEAS, 0f)
        val result = storage.removeContainer(Cereal.PEAS)
        assertTrue(result)
    }

    @Test
    fun `removeContainer return false if container does not exist`() {
        val result = storage.removeContainer(Cereal.PEAS)
        assertFalse(result)
    }

    @Test
    fun `removeContainer return false if container not empty`() {
        storage.addCereal(Cereal.PEAS, 1f)
        val result = storage.removeContainer(Cereal.PEAS)
        assertFalse(result)
        assertEquals(1f, storage.getAmount(Cereal.PEAS))
    }

    //getCereal
    @Test
    fun `getCereal return correct amount when taking less`() {
        storage.addCereal(Cereal.RICE, 5f)
        val takenAmount = storage.getCereal(Cereal.RICE, 3f)
        assertEquals(3f, takenAmount)
        assertEquals(2f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `getCereal return 0 amount when taking more than actual amount`() {
        storage.addCereal(Cereal.RICE, 4f)
        val takenAmount = storage.getCereal(Cereal.RICE, 5f)
        assertEquals(4f, takenAmount)
        assertEquals(0f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `getCereal return 0 amount when container is missing`() {
        val takenAmount = storage.getCereal(Cereal.RICE, 1f)
        assertEquals(0f, takenAmount)
        assertEquals(0f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `getCereal throws IllegalArgumentException when cereal amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.RICE, -1f)
        }
    }

    @Test
    fun `getCereal return correct amount of leftover when enough cereal is taken`() {
        storage.addCereal(Cereal.RICE, 10f)
        val leftover = storage.getCereal(Cereal.RICE, 3f)
        assertEquals(3f, leftover)
        assertEquals(7f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `getCereal return less amount of leftover when more cereal is taken`() {
        storage.addCereal(Cereal.RICE, 6f)
        val leftover = storage.getCereal(Cereal.RICE, 7f)
        assertEquals(6f, leftover)
        assertEquals(0f, storage.getAmount(Cereal.RICE))
    }

    //addCereal
    @Test
    fun `addCereal return 0 when added cereal to existing container`() {
        storage.addCereal(Cereal.RICE, 10f)
        val freeSpaceBefore = storage.getSpace(Cereal.RICE)
        val result = storage.addCereal(Cereal.RICE, freeSpaceBefore)
        assertEquals(0f, result)
        assertEquals(0f, storage.getSpace(Cereal.RICE))
    }

    @Test
    fun `addCereal return amount when added cereal to almost full container`() {
        storage.addCereal(Cereal.RICE, 5f)
        val result = storage.addCereal(Cereal.RICE, 6f)
        assertEquals(1f, result)
        assertEquals(0f, storage.getSpace(Cereal.RICE))
    }

    @Test
    fun `addCereal return new container with added cereal`() {
        storage.addCereal(Cereal.RICE, 1f)
        val leftover = storage.addCereal(Cereal.BULGUR, 2f)
        assertEquals(0f, leftover)
        assertEquals(2f, storage.getAmount(Cereal.BULGUR))
        assertEquals(1f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `addCereal return 0 and updates existing container`() {
        storage.addCereal(Cereal.RICE, 5f)
        val leftover = storage.addCereal(Cereal.RICE, 3f)
        assertEquals(8f, storage.getAmount(Cereal.RICE))
        assertEquals(0f, leftover)
    }

    @Test
    fun `addCereal return leftover amount when container capacity is full`() {
        storage.addCereal(Cereal.RICE, 6f)
        val leftover = storage.addCereal(Cereal.RICE, 5f)
        assertEquals(1f, leftover)
        assertEquals(storage.containerCapacity, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `addCereal throws IllegalArgumentException when added amount less than 0`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.RICE, -1f)
        }
    }

    @Test
    fun `addCereal throws IllegalStateException when storage have no place for another container`() {
        storage.apply {
            addCereal(Cereal.RICE, 4f)
            addCereal(Cereal.BULGUR, 4f)
        }
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.PEAS, 4f)
        }
    }

    //toString
    @Test
    fun `toString return result in text`() {
        storage.addCereal(Cereal.RICE, 1f)
        val result = storage.toString()
        assertTrue(result.isNotBlank())
    }

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }
}
