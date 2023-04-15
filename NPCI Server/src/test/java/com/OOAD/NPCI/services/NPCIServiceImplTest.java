package com.OOAD.NPCI.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.OOAD.NPCI.Repositories.BankAccountRepository;
import com.OOAD.NPCI.Repositories.BankServersRepository;
import com.OOAD.NPCI.Repositories.NPCIAccountRepository;
import com.OOAD.NPCI.domain.BankServerEntity;
import com.OOAD.NPCI.domain.NPCIAccount;
import com.OOAD.NPCI.services.impl.NPCIServiceImpl;

@ExtendWith(MockitoExtension.class)
public class NPCIServiceImplTest {
    @Mock
    private NPCIAccountRepository NPCIRep;

    @Mock
    private BankAccountRepository BankRep;

    @Mock
    private BankServersRepository ServersRep;

    @InjectMocks
    private NPCIServiceImpl myImpl;

    @Test 
    public void testThatRegisterWorks(){

        NPCIAccount NPCIAccEntity = NPCIAccount.builder()
        .phoneNumber("1234567890")
        .defaultBankAccNumber("1234567890")
        .defaultBank("HDFC")
        .build();

        NPCIAccount fullAcc = NPCIAccount.builder()
        .upiId("MyUpiId@NPCI")
        .phoneNumber("1234567890")
        .defaultBankAccNumber("1234567890")
        .defaultBank("HDFC")
        .build();

        BankServerEntity myServer = new BankServerEntity("HDFC", "http://127.0.0.1:5000", "alkjdhfalkhfda");

        when(NPCIRep.save(eq(NPCIAccEntity))).thenReturn(fullAcc);
        when(ServersRep.findBybankName("HDFC")).thenReturn(myServer);

        NPCIAccount returnVal = myImpl.registerAccount("1234567890", "1234567890", "HDFC");
        assertEquals(returnVal.getPhoneNumber(), "1234567890");

        
    }


}
