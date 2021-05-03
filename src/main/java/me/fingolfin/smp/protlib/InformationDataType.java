package me.fingolfin.smp.protlib;

import org.apache.commons.lang.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class InformationDataType implements PersistentDataType<byte[], Information> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Information> getComplexType() {
        return Information.class;
    }

    @Override
    public byte[] toPrimitive(Information information, PersistentDataAdapterContext persistentDataAdapterContext) {
        return SerializationUtils.serialize(information);
    }

    @Override
    public Information fromPrimitive(byte[] bytes, PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            InputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(is);
            return (Information) o.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
