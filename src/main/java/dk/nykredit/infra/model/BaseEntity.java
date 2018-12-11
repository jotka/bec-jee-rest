package dk.nykredit.infra.model;

import java.io.Serializable;


public interface BaseEntity extends Serializable {
    <T extends Serializable> T getId();
}
