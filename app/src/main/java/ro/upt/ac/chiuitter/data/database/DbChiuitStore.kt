package ro.upt.ac.chiuitter.data.database

import ro.upt.ac.chiuitter.data.ChiuitRepository
import ro.upt.ac.chiuitter.domain.Chiuit

class DbChiuitStore(private val appDatabase: AppDatabase) : ChiuitRepository {

    override fun getAll(): List<Chiuit> {
        return appDatabase.chiuitDao().getAll().map { it.toDomainModel() }
    }

    override fun addChiuit(chiuit: Chiuit) {
        appDatabase.chiuitDao().insert(chiuit.toDbModel())
    }

    override fun removeChiuit(chiuit: Chiuit) {
        appDatabase.chiuitDao().delete(chiuit.toDbModel())
    }

    private fun Chiuit.toDbModel() = ChiuitEntity(timestamp, description)

    private fun ChiuitEntity.toDomainModel() = Chiuit(timestamp, description)

}