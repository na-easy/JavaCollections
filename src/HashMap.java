public class HashMap<K, V> {
    /**
     * Хэш-таблица (массив bucket'ов).
     */
    Node<K, V>[] table;

    /**
     * Количество пар <Ключ, Значение>.
     */
    int size;

    /**
     * Предельное значение элементов, при котором меняется размер таблицы.
     */
    int threshold;

    /**
     * Степень загруженности хэш-таблицы.
     */
    final float loadFactor;

    /**
     * Конструктор по умолчанию, который создает пустую hashMap с начальными значениями:
     * capacity(размер хэш-таблицы) = 16
     * loadFactor(коэффициент загруженности) = 0.75
     */
    public HashMap() {
        this.table = new Node[16];
        this.loadFactor = 0.75F;
        this.threshold = (int) (table.length * loadFactor);
    }

    /**
     * Конструктор с параметром вместимости, который создает пустую hashMap со значениями:
     * loadFactor(коэффициент загруженности) = 0.75
     *
     * @param capacity - размер хэш-таблицы
     */
    public HashMap(int capacity) {
        this(capacity, 0.75F);
    }

    /**
     * Конструктор с параметрами, который проверяет корректность переданных параметров.
     * Если передаваемый размер хэш-таблицы меньше 0, то выбрасывается исключение.
     * Если передаваемый коэффициент загруженности таблицы меньше или равен нулю,
     * или не является float, то выбрасывается исключение.
     *
     * @param capacity   - размер хэш-таблицы
     * @param loadFactor - коэффициент загруженности хэш-таблицы
     */
    public HashMap(int capacity, float loadFactor) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException();
        this.table = new Node[capacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
    }

    /**
     * Возвращает количество пар <Ключ, Значение> в таблице.
     */
    public int size() {
        return this.size;
    }

    /**
     * Возвращает true, если таблица пуста.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Возвращает соответствующий номер bucket'а по хеш-коду.
     *
     * @param h        - хеш-код
     * @param capacity - количество bucket'ов
     */
    private int indexFor(int h, int capacity) {
        return h % (capacity - 1);
    }

    /**
     * Добавляет новую Node с указанным ключом, значением и хэш-кодом в указанный bucket.
     * При достижении предельного значения таблицы метод увеличивает размер таблицы вдвое.
     *
     * @param hash        - хеш-код ключа
     * @param key         - ключ
     * @param value       - значение
     * @param bucketIndex - номер bucket'а для новой Node
     */
    private void addNode(int hash, K key, V value, int bucketIndex) {
        Node<K, V> n = table[bucketIndex];
        table[bucketIndex] = new Node<>(hash, key, value, n);
        if (size++ >= threshold)
            resize(2 * table.length);
    }

    /**
     * Добавляет пару <Ключ, Значение> в таблицу.
     * Для добавления новой пары считается хэш-код для ключа и определяется bucket.
     * В найденном bucket'е сравниваем хэш-коды и ключи с переданными.
     * Если для данного ключа уже было значение, то оно заменится на новое.
     * Если совпадений не было, то создастся новая Node и добавится в конец bucket'а.
     *
     * @param key   - ключ для добавления
     * @param value - значение для добавления
     * @return предыдущее значение связанное с данным ключом или null, если такого
     * ключа не было в таблице до этого. Null также может означать предыдущее
     * значение, связанное с данным ключом.
     */
    public V put(K key, V value) {
        if (key == null)
            return putForNullKey(value);
        int hashKey = key.hashCode();
        int i = indexFor(hashKey, table.length);
        for (Node<K, V> n = table[i]; n != null; n = n.next) {
            Object k;
            if (n.hash == hashKey && ((k = n.key) == key || key.equals(k))) {
                V oldValue = n.value;
                n.value = value;
                return oldValue;
            }
        }
        addNode(hashKey, key, value, i);
        return null;
    }

    /**
     * Добавление Node с ключом равным null в bucket 0.
     *
     * @param value - значение для ключа равного null
     * @return предыдущее значение связанное с null ключом или null, если такого
     * ключа не было в таблице до этого. Null также может означать предыдущее
     * значение, связанное с null ключом.
     */
    private V putForNullKey(V value) {
        for (Node<K, V> n = table[0]; n != null; n = n.next) {
            if (n.key == null) {
                V oldValue = n.value;
                n.value = value;
                return oldValue;
            }
        }
        addNode(0, null, value, 0);
        return null;
    }

    /**
     * Возвращает значение искомого ключа или null, если такой ключ не был найден.
     * Для поиска ключа считается хэш-код переданного ключа и определяется bucket.
     * В найденном bucket'е сравниваются хэш-коды с искомым хэш-кодом.
     * Если хэш-коды равны, то сравниваются ключи. Если ключи равны, то значение найдено.
     *
     * @param key - искомый ключ
     */
    public V get(K key) {
        if (key == null)
            return getForNullKey();
        int hashKey = key.hashCode();
        int index = indexFor(hashKey, table.length);
        for (Node<K, V> n = table[index]; n != null; n = n.next) {
            K k;
            if (n.hash == hashKey && ((k = n.key) == key || key.equals(k)))
                return n.value;
        }
        return null;
    }

    /**
     * Возвращает значение для ключа равного null.
     * Поиск null ключа осуществляется в bucket'е 0.
     */
     private V getForNullKey() {
        for (Node<K,V> n = table[0]; n != null; n = n.next) {
            if (n.key == null)
                return n.value;
        }
        return null;
    }

    /**
     * Удаляет пару <Ключ, Значение>, если она есть.
     * Вычисляем номер bucket'а по ключу. В найденном bucket'е ищем по хэшу и ключу
     * искомую для удаления пару. Если пара нашлась, то уменьшаем количество элементов на 1
     * и удаляем пару путем переопределения ссылок.
     *
     * @param key - ключ для удаления
     * @return значение удаленного ключа или null, если ключ не был найден
     */
    public V remove(K key) {
        int hashKey = (key == null) ? 0 : key.hashCode();
        int index = indexFor(hashKey, table.length);
        Node<K, V> prev = table[index];
        Node<K, V> n = prev;
        while (n != null) {
            Node<K, V> next = n.next;
            Object k;
            if (n.hash == hashKey && ((k = n.key) == key || key.equals(k))) {
                size--;
                if (prev == n)
                    table[index] = next;
                else
                    prev.next = next;
            }
            prev = n;
            n = next;
        }
        return (n == null ? null : n.value);
    }

    /**
     * Удаляет все данные в хэш-таблице.
     * Хэш-таблица после вызова метода будет пустой.
     */
    public void clear() {
        Node<K, V>[] tab = table;
        for (int i = 0; i < tab.length; i++)
            tab[i] = null;
        size = 0;
    }

    /**
     * Возвращает true, если в хэш-таблице содержится переданный ключ.
     *
     * @param key - искомый ключ
     */
    public boolean containsKey(K key) {
        if (key == null)
            throw new NullPointerException();

        Node<K, V>[] tab = table;
        for (Node<K, V> kvNode : tab)
            for (Node<K, V> n = kvNode; n != null; n = n.next)
                if (key.equals(n.key))
                    return true;
        return false;
    }

    /**
     * Возвращает true, если в хэш-таблице содержится переданное значение.
     *
     * @param value - искомое значение
     */
    public boolean containsValue(V value) {
        if (value == null)
            throw new NullPointerException();

        Node<K, V>[] tab = table;
        for (Node<K, V> kvNode : tab)
            for (Node<K, V> n = kvNode; n != null; n = n.next)
                if (value.equals(n.value))
                    return true;
        return false;
    }

    /**
     * Перераспределяет содержимое хэш-таблицы в таблицу большего размера.
     * Для этого создается пустая таблица с новой вместимостью.
     * В цикле обходится каждый bucket и считается новый хэш, с помощью которого
     * пара размещается в новой таблице.
     * Пересчитывается предельное количество элементов.
     *
     * @param newCapacity - новый размер хэш-таблицы
     */
    void resize(int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> n = table[i];
            if (n != null) {
                table[i] = null;
                do {
                    Node<K, V> next = n.next;
                    int index = indexFor(n.hash, newCapacity);
                    n.next = newTable[index];
                    newTable[index] = n;
                    n = next;
                } while (n != null);
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }
}
