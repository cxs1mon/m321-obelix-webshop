package ch.bbw.obelix.quarry.service;

import ch.bbw.obelix.quarry.entity.MenhirEntity;
import ch.bbw.obelix.quarry.repository.MenhirRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class QuarryService {

    @Lazy
    private final MenhirRepository menhirRepository;

    @PostConstruct
    public void initializeMenhirs() {
        if (menhirRepository.count() == 0) {
            createDefaultMenhirs();
        }
    }

    public void createDefaultMenhirs() {
        menhirRepository.deleteAll();

        var obelixSpecial = new MenhirEntity();
        obelixSpecial.setWeight(2.5);
        obelixSpecial.setStoneType("Granite Gaulois");
        obelixSpecial.setDecorativeness(MenhirEntity.Decorativeness.DECORATED);
        obelixSpecial.setDescription("Obelix's personal favorite! Perfect for throwing at Romans. ");
        menhirRepository.save(obelixSpecial);

        var getafixMasterpiece = new MenhirEntity();
        getafixMasterpiece.setWeight(4.2);
        getafixMasterpiece.setStoneType("Mystical Dolmen Stone");
        getafixMasterpiece.setDecorativeness(MenhirEntity.Decorativeness.MASTERWORK);
        getafixMasterpiece.setDescription("Blessed by Getafix himself! ...");
        menhirRepository.save(getafixMasterpiece);

        var touristTrap = new MenhirEntity();
        touristTrap.setWeight(1.0);
        touristTrap.setStoneType("Imported Roman Marble");
        touristTrap.setDecorativeness(MenhirEntity.Decorativeness.PLAIN);
        touristTrap.setDescription("Budget-friendly option! ...");
        menhirRepository.save(touristTrap);
    }
}