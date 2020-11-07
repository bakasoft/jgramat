package stone.examiners;

public interface ExaminerRepository {
    Examiner findExaminer(Class<?> type);
}
