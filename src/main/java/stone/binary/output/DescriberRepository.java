package stone.binary.output;

public interface DescriberRepository {

    ObjectDescriber findDescriberFor(Object obj);

    default BinarySerializer createSerializer() {
        return new BinarySerializer(this);
    }

    default BinarySerializer createSerializer(ReferenceStrategy strategy) {
        return new BinarySerializer(this, strategy);
    }

}
