package stone.examiners;

public interface ArrayExaminer extends Examiner {

    int getSizeOf(Object value);

    Object getValueAt(int index, Object value);

}
