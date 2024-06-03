package moe.sgs.kt.shuntingyard

/**
 * A [HashMap] backed [MutableMap] implementing [Transactional]
 *
 * @param K the type of map keys. The map is invariant in its key type.
 * @param V the type of map values. The mutable map is invariant in its value type.
 */
class TransactionalHashMap<K, V>() : MutableMap<K, V>, Transactional {
    private var hashMap = HashMap<K, V>()
    private var abortMap = HashMap<K, V>(hashMap)
    private var isTransactionActive = false

    constructor(initialCapacity: Int) : this() {
        hashMap = HashMap(initialCapacity)
        abortMap = HashMap(hashMap)
    }

    override fun isTransactionActive() = isTransactionActive

    override fun commit() {
        if (isTransactionActive) {
            abortMap = HashMap(hashMap)
            isTransactionActive = false
        }
    }

    override fun abort() {
        if (isTransactionActive) {
            hashMap = HashMap(abortMap)
            isTransactionActive = false
        }
    }

    override val size: Int
        get() = hashMap.size
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() {
            isTransactionActive = true
            return hashMap.entries
        }
    override val keys: MutableSet<K>
        get() {
            isTransactionActive = true
            return hashMap.keys
        }
    override val values: MutableCollection<V>
        get() {
            isTransactionActive = true
            return hashMap.values
        }

    override fun clear() {
        isTransactionActive = true
        hashMap.clear()
    }

    override fun containsKey(key: K): Boolean = hashMap.containsKey(key)

    override fun containsValue(value: V): Boolean = hashMap.containsValue(value)

    override fun get(key: K): V? {
        return when (val elm = hashMap[key]) {
            null -> null
            else -> {
                isTransactionActive = true
                elm
            }
        }
    }

    override fun isEmpty(): Boolean = hashMap.isEmpty()

    override fun put(key: K, value: V): V? {
        isTransactionActive = true
        return hashMap.put(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
        isTransactionActive = true
        hashMap.putAll(from)
    }

    override fun remove(key: K): V? {
        isTransactionActive = true
        return hashMap.remove(key)
    }
}


/**
 * Returns an empty new [TransactionalHashMap].
 */
inline fun <K, V> transactionalHashMapOf(): TransactionalHashMap<K, V> = TransactionalHashMap()

/**
 * Returns a new [TransactionalHashMap] with the specified contents, given as a list of pairs
 * where the first component is the key and the second is the value.
 */
fun <K, V> transactionalHashMapOf(vararg pairs: Pair<K, V>): TransactionalHashMap<K, V> =
    TransactionalHashMap<K, V>(pairs.size).apply {
        putAll(pairs)
        commit()
    }
