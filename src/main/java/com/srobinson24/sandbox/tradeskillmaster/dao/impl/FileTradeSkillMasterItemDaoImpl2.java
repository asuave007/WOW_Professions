package com.srobinson24.sandbox.tradeskillmaster.dao.impl;

import com.google.common.collect.Maps;
import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeFileProcessingException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FileTradeSkillMasterItemDaoImpl2 implements TradeSkillMasterItemDao {

    private final Logger logger = LoggerFactory.getLogger(FileTradeSkillMasterItemDaoImpl2.class);

    @Value("${save.file}")
    private String fileName;

    private final File saveFile;

    @Autowired
    FileTradeSkillMasterItemDaoImpl2(final File saveFile) {
        this.saveFile = saveFile;
    }


    @Override
    public void saveAll(Map<Integer, TradeSkillMasterItem> map) {
        try {
            FileUtils.touch(saveFile); //creates the saveFile if it does not exist
        } catch (IOException ex) {
            throw new RuntimeFileProcessingException("Could not create saveFile: " + saveFile.getAbsolutePath(), ex);
        }
        //next line opens the file AND deletes all the file's contents
        try (final ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(saveFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING))){
            oos.writeObject(map); // this is the actual save back to the saveFile
            oos.flush();
        } catch (IOException ex) {
            throw new RuntimeFileProcessingException("IO exception for saveFile: " + saveFile.getAbsolutePath(), ex);
        }
    }

    public void save(TradeSkillMasterItem tsmItem) {
        try {
            FileUtils.touch(saveFile); //creates the saveFile if it does not exist
        } catch (IOException ex) {
            throw new RuntimeFileProcessingException("Could not create saveFile: " + saveFile.getAbsolutePath(), ex);
        }
        final Map<Integer, TradeSkillMasterItem> itemMap = readAll(); // read from saveFile
        //next line opens the file AND deletes all the file's contents
        try (final ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(saveFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING))){
            itemMap.put(tsmItem.getId(), tsmItem); // save to map
            oos.writeObject(itemMap); // this is the actual save back to the saveFile
            oos.flush();
        } catch (IOException ex) {
            throw new RuntimeFileProcessingException("IO exception for saveFile: " + saveFile.getAbsolutePath(), ex);
        }
    }


    @Override
    public TradeSkillMasterItem read(Integer id) {
        final Map<Integer, TradeSkillMasterItem> itemMap = readAll();
        return itemMap.get(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Integer, TradeSkillMasterItem> readAll () {
        if (!saveFile.exists()) return Maps.newHashMap();

        try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            return Map.class.<Integer, TradeSkillMasterItem>cast(ois.readObject());
        } catch (EOFException ex) {
            //this happens if the file is empty
            logger.error("THIS NEEDS FIXED, WHY AM I HERE!", ex);
            return Maps.newHashMap(); //fixme: REALLY??!?! expected behaviour????
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeFileProcessingException(ex);
        }

    }

}
