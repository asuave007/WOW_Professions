package com.srobinson24.sandbox.tradeskillmaster.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeFileException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 3/28/2017.
 */
@Repository
public class FileTradeSkillMasterItemDaoImpl implements TradeSkillMasterItemDao {

    @Value("${save.file}")
    private String fileName;

    @Override
    public boolean saveAll (Set<TradeSkillMasterItem> tradeSkillMasterItemSet) {

        for (TradeSkillMasterItem tradeSkillMasterItem : tradeSkillMasterItemSet) {
            save(tradeSkillMasterItem);
        }
        return true;
    }

    @Override
    public boolean save(TradeSkillMasterItem tsmItem) {
        final File saveFile = new File(fileName);
        try {
            FileUtils.touch(saveFile); //creates the saveFile if it does not exist
        } catch (IOException ex) {
            throw new RuntimeFileException("Could not create saveFile: " + saveFile.getAbsolutePath(), ex);
        }
        final Map<Integer, TradeSkillMasterItem> itemMap = readAll(); // read from saveFile
        //next line opens the file AND deletes all the file's contents
        try (final ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(saveFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING))){
            itemMap.put(tsmItem.getId(), tsmItem); // save to map
            oos.writeObject(itemMap); // this is the actual save back to the saveFile
            oos.flush();
        } catch (IOException ex) {
            throw new RuntimeFileException("IO exception for saveFile: " + saveFile.getAbsolutePath(), ex);
        }
        return true;
    }


    @Override
    public TradeSkillMasterItem read(Integer id) {
        final Map<Integer, TradeSkillMasterItem> itemMap = readAll();
        return itemMap.get(id);
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> readAll () {
        final File file = new File(fileName);
        if (!file.exists()) return Maps.newHashMap();

        try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return Map.class.<Integer, TradeSkillMasterItem>cast(ois.readObject());
        } catch (EOFException ex) {
            return Maps.newHashMap(); //fixme: REALLY??!?! expected behaviour????
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeFileException(ex);
        }

    }

    @Override
    public boolean delete(TradeSkillMasterItem tsmItem) {
        Preconditions.checkNotNull(tsmItem);
        final File saveFile = new File(fileName);
        final Map<Integer, TradeSkillMasterItem> itemMap = readAll(); // read from saveFile
        //next line opens the file AND deletes all the file's contents
        try (final ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(saveFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING))){
            final TradeSkillMasterItem removedItem = itemMap.remove(tsmItem.getId());
            oos.writeObject(itemMap); // write object back out
            oos.flush();
            return tsmItem.equals(removedItem) || removedItem == null; // should always be true
        } catch (IOException ex) {
            throw new RuntimeFileException("IO exception for saveFile: " + saveFile.getAbsolutePath(), ex);
        }
    }

}
