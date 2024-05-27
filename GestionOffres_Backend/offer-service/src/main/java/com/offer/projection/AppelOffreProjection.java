package com.offer.projection;

import java.util.Date;

public interface AppelOffreProjection {
    String getTitre();
    Date getDatecreation();
    Date getDatelimitesoumission();
    String getLocalisation();
    byte[] getImg();
}
