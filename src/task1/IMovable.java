package task1;

// Стратегия перемещения героя
public interface IMovable {

    // Название способа перемещения (для списка выбора)
    String getName();

    // Глагол, описывающий действие: "идёт", "скачет" ...
    String move();

    // Скорость перемещения, км/ч
    double getSpeed();
}
