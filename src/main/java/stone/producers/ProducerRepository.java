package stone.producers;

public interface ProducerRepository {
    ObjectProducer findObjectMaker(String type);

    ArrayProducer findArrayMaker(String type);

    ValueProducer findValueMaker(String type);
}
