package pl.edu.pjatk.fcl;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.Gpr;

@RequiredArgsConstructor
public class FisService {
    private final String fclFileName;

    /**
     * tries to load Fuzzy Interface System using fcl file loader
     *
     * @return Try of FIS on success state or Try of Throwable on failure
     */
    public Try<FIS> tryLoadFisFromFclFile() {
        return Try.of(() -> FIS.load(fclFileName, true))
                .onFailure(throwable -> Gpr.debug(throwable.getMessage()));
    }
}
