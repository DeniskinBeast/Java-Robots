package log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Что починить: 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти) 2. Этот класс хранит активные сообщения лога, но в такой реализации он их
 * лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено величиной
 * m_iQueueLength (т.е. реально нужна очередь сообщений ограниченного размера)
 */
public class LogWindowSource {

    private int m_iQueueLength;

    private ArrayDeque<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayDeque<>(iQueueLength);
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (m_messages.size() == m_iQueueLength) {
            m_messages.remove();
        }
        m_messages.add(entry);
        synchronized (m_listeners) {
            for (LogChangeListener listener : m_listeners) {
                listener.onLogChanged();
            }
        }
    }

    public int size() {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return new ArrayList<>(m_messages).subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return m_messages;
    }
}
