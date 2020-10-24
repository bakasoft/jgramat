package gramat.machine.binary;

import gramat.binary.BinaryWriter;
import gramat.machine.State;

import java.io.IOException;
import java.io.OutputStream;

public class StateSerializer {

    private static final int MAGIC_0 = 'G';
    private static final int MAGIC_1 = 'M';
    private static final int MAGIC_2 = 'B';
    private static final int MAGIC_3_VER_MAJ = 1;
    private static final int MAGIC_4_VER_MIN = 0;

    

    public static void write(State state, OutputStream output) throws IOException {
        output.write(MAGIC_0);
        output.write(MAGIC_1);
        output.write(MAGIC_2);
        output.write(MAGIC_3_VER_MAJ);
        output.write(MAGIC_4_VER_MIN);

        var repository = new ModelRepository();
        var writer = repository.createWriter(output);


        writer.writeObject(state);
    }

}
