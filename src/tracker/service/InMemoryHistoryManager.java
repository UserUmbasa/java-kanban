package tracker.service;

import tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    //----история просмотренных задач-------
    protected List<Task> historyTask = new ArrayList<>();
    Map<Integer, Node<Task>> mapHistory = new HashMap<>();

    @Override
    public void add(Task task) {
        if (mapHistory.containsKey(task.getId())) {
            //получили ссылку на цель
            Node<Task> nodeTarget = mapHistory.get(task.getId());
            if (tail == nodeTarget) { //если цель это хвост, выход
                return;
            } else if (head == nodeTarget) { //если цель это голова (хвост на нее не указывает)
                //получаем правую ноду
                head = nodeTarget.next; //делаем ее головой
                head.prev = null;
                //добавляем в конец списка
                linkLast(nodeTarget);
            } else { //если цель в промежутке, получили левую и правую ноды от цели
                Node<Task> nodeLhs = nodeTarget.prev;
                Node<Task> nodeRhs = nodeTarget.next;
                //меняем указатели
                nodeLhs.next = nodeRhs;
                nodeRhs.prev = nodeLhs;
                //добавляем в конец списка
                linkLast(nodeTarget);
            }
            historyTask.clear(); // сброс буфера
        } else {
            final Node<Task> newNode;
            if (mapHistory.isEmpty()) {
                newNode = new Node<>(null, task, null);
                head = newNode;
                tail = newNode;
            } else {
                newNode = new Node<>(tail, task, null);
                linkLast(newNode);
            }
            mapHistory.put(task.getId(), newNode);
            historyTask.add(task);
        }
    }

    //Добавьте вызов метода при удалении задач, чтобы они удалялись также из истории просмотров.
    @Override
    public void remove(int id) {
        if (mapHistory.containsKey(id)) {
            if (mapHistory.size() == 1) {
                mapHistory.clear();
                return;
            }
            //получили ссылку на цель
            Node<Task> nodeTarget = mapHistory.get(id);
            if (tail == nodeTarget) { //если цель это хвост
                tail = nodeTarget.prev;
                tail.next = null;
                mapHistory.remove(id);
            } else if (head == nodeTarget) { //если цель это голова (хвост на нее не указывает)
                head = nodeTarget.next;
                head.prev = null;
                mapHistory.remove(id);
            } else { //если цель в промежутке, получили левую и правую ноды от цели
                Node<Task> nodeLhs = nodeTarget.prev;
                Node<Task> nodeRhs = nodeTarget.next;
                //меняем указатели
                nodeLhs.next = nodeRhs;
                nodeRhs.prev = nodeLhs;
                mapHistory.remove(id);
            }
            historyTask.clear(); // сброс буфера
        }
    }

    private void linkLast(Node<Task> target) { //будет добавлять задачу в конец списка
        Node<Task> oldTail = tail;
        oldTail.next = target;
        tail = target;
        tail.next = null;
        tail.prev = oldTail;
    }

    //Реализация метода getHistory должна перекладывать задачи из связного списка в ArrayList для формирования ответа.
    @Override
    public List<Task> getHistory() {
        if (historyTask.isEmpty()) {
            Node<Task> current = head; // начинаем с головы
            while (current != null) { // пока не дойдем до конца списка
                historyTask.add(current.data);
                current = current.next; // переходим к следующему узлу
            }
        }
        return List.copyOf(historyTask);
    }

    /**
     * Указатель на первый элемент списка. Он же first
     */
    private Node<Task> head;
    /**
     * Указатель на последний элемент списка. Он же last
     */
    private Node<Task> tail;

    private static class Node<E> {
        //данные
        public E data;
        /**
         * Указатель на следующий элемент
         */
        public Node<E> next;
        /**
         * Указатель на предыдущий элемент
         */
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
