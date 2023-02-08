package no.liflig.ks2kurs.common.db

sealed class DaoException : RuntimeException() {
  object ItemAlreadyExists : DaoException()
}

interface EntityId

interface Entity<ID : EntityId> {
  val id: ID
}

class CrudDao<I : Entity<ID>, ID : EntityId> {

  private val items: MutableList<I> = mutableListOf()
  fun create(item: I): I {
    if (items.firstOrNull { it.id == item.id } != null) {
      throw DaoException.ItemAlreadyExists
    }
    items.add(item)
    
    return item
  }

  fun get(itemId: ID): I? {
    return items.firstOrNull { it.id == itemId }
  }

  fun update(item: I): I {
    // The items should not match but have same id
    val existingItem = items.find { it.id == item.id }
    val index = items.indexOf(existingItem)
    items[index] = item
    return item
  }

  fun delete(item: I) {
    items.filter { it.id != item.id }.toMutableList()
  }

  fun getAll(): List<I> = items
}
