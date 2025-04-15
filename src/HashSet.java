public class HashSet<E> {
    /**
     * Внутренний hashMap, которому делегируются все методы.
     */
    private final HashMap<E, Object> map;

    /**
     * Константа-заглушка для значения.
     */
    private static final Object PRESENT = new Object();

    /**
     * Конструктор по умолчанию, который инициализирует пустую hashMap с начальными значениями:
     * capacity(размер хэш-таблицы) = 16
     * loadFactor(коэффициент загруженности) = 0.75
     */
    public HashSet() {
        this.map = new HashMap<>();
    }

    /**
     * Конструктор с параметром вместимости, который создает пустую hashMap со значениями:
     * loadFactor(коэффициент загруженности) = 0.75
     *
     * @param capacity - размер хэш-таблицы
     */
    public HashSet(int capacity) {
        this.map = new HashMap<>(capacity);
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
    public HashSet(int capacity, float loadFactor) {
        this.map = new HashMap<>(capacity, loadFactor);
    }

    /**
     * Возвращает количество элементов в коллекции.
     */
    public int size() {
        return map.size();
    }

    /**
     * Возвращает true, если коллекция пуста.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Добавляет элемент в коллекцию, если его в ней не было.
     * Если такой элемент уже есть, метод возвращает false.
     *
     * @param e - новый элемент
     */
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    /**
     * Удаляет данный элемент из коллекции, если он есть.
     * Возвращает true, если элемент был удален.
     *
     * @param e - удаляемый элемент
     */
    public boolean remove(E e) {
        return map.remove(e) == PRESENT;
    }

    /**
     * Удаляет все данные в коллекции.
     * Коллекция после вызова метода будет пустой.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Возвращает true, если в коллекции содержится переданный элемент.
     *
     * @param e - искомый элемент
     */
    public boolean contains(E e) {
        return map.containsKey(e);
    }
}
