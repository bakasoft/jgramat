package gramat.actions;

import gramat.eval.Context;

import java.util.List;

public interface Action {

    void run(Context context);

    String getName();

    List<String> getArguments();

}
