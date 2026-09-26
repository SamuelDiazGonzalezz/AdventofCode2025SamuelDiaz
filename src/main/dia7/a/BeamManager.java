package dia7.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class BeamManager {
    private final List<Beam> beamList;
    private static BeamManager instance;

    private BeamManager() {
        beamList = new ArrayList<>();
    }

    public static BeamManager getInstance() {
        if (instance == null) instance = new BeamManager();
        return instance;
    }

    public static void resetInstance() {
        instance = new BeamManager();
    }

    public BeamManager parse(String puzzle) {
        return parse(puzzle.split("\n"));
    }

    private BeamManager parse(String[] split) {
        beamList.add(Beam
                .create(0, split[0].length())
                .add(new Position(0, split[0].indexOf('S')))
        );
        return parse(Arrays.asList(split).subList(1, split.length));
    }

    private BeamManager parse(List<String> strings) {
        IntStream
                .range(0, strings.size())
                .mapToObj(i -> beamList
                        .get(i)
                        .next(strings.get(i))
                )
                .forEach(beamList::add);

        return this;
    }

    public long count() {
        return beamList.getLast().count();
    }

    public long splits() {
        return beamList.getLast().splits();
    }
}
