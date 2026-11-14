/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.fon.veterinarska.server.controller;

import java.util.List;
import rs.fon.veterinarska.common.communication.ClientRequest;
import rs.fon.veterinarska.common.communication.ResponseStatus;
import rs.fon.veterinarska.common.communication.ServerResponse;
import rs.fon.veterinarska.common.constants.Operations;
import rs.fon.veterinarska.common.domain.VrstaZivotinje;
import rs.fon.veterinarska.server.db.DBConnectionFactory;
import rs.fon.veterinarska.server.so.izvestaj.SOVratiListuIzvestajaOPregledu;
import rs.fon.veterinarska.server.so.vrstazivotinje.SOVratiListuSvihVrstaZivotinja;

/**
 *
 * @author nenad
 */
public class ServerController {
    public static ServerResponse odgovoriKlijentu(ClientRequest req) throws Exception {
        ServerResponse res = new ServerResponse();
        
        switch(req.getOperation()) {
            case Operations.VRATI_LISTU_SVIH_VRSTA_ZIVOTINJA:
                System.out.println("dosao do slucaja");
                SOVratiListuSvihVrstaZivotinja so = 
                        new SOVratiListuSvihVrstaZivotinja(new DBConnectionFactory("jdbc:mysql://localhost:3306/veterinarska_ordinacija", "root", "Luma2410#"));
                System.out.println("Parametar je " + req.getParameter());
                so.execute(null);
                
                System.out.println("napravio so");
                List<VrstaZivotinje> rezultat = so.getResult();
                System.out.println("dobio rezultat");
                res.setResponse(rezultat);
                res.setStatus(ResponseStatus.SUCCESS);
                res.setMessage("Uspeno");
                break;
            default:
                break;
        }
        System.out.println("odgovorio klijentu");
        return res;
    }
}
