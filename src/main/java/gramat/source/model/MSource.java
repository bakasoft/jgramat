package gramat.source.model;

import gramat.source.formatting.SourceWriter;

import java.util.List;

public class MSource implements MElement {

    public List<MSourceMember> members;

    @Override
    public void write(SourceWriter writer) {
        for (var member : members) {
            member.write(writer);
        }
    }
}
