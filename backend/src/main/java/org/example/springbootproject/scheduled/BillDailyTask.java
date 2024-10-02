package org.example.springbootproject.scheduled;

import org.example.springbootproject.entity.Bill;
import org.example.springbootproject.entity.Contract;
import org.example.springbootproject.entity.Room;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.service.BillService;
import org.example.springbootproject.service.ContractService;
import org.example.springbootproject.service.EmailService;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BillDailyTask extends BaseTask {

    @Autowired
    private BillService billService;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "${task.dailyCron}")
    public void runDailyTaskAtMidnight() {
        Map<String, List<Bill>> listData = billService.handleCheckBillDaily();
        List<Contract> listOverdueContracts = listData.get("listOverdueContracts");
        contractService.changeListContractStatus(listOverdueContracts, Constants.CONTRACT_STATUS_TERMINATED);
        List<Contract> listRenewedContracts = listData.get("listRenewedContracts");
        contractService.changeListContractStatus(listRenewedContracts, Constants.CONTRACT_STATUS_RENEWED);
        handleContracts(listOverdueContracts, "Contract Termination Notice", "contract_terminate_en");
        handleContracts(listRenewedContracts, "Contract Renewal Notice", "contract_renew_en");
    }

    public void handleContracts(List<Contract> listContracts, String subject, String templateName) {
        try {
            for (Contract overdueContract : listContracts) {
                Room room = overdueContract.getRoom();
                Set<User> tenants = room.getRoomsTenants();
                User landlord = room.getLandlord();

                String[] emailTo = tenants.stream().map(User::getEmail).toArray(String[]::new);

                Map<String, String> emailData = new HashMap<>();
                emailData.put("tenants", tenants.stream().map(User::getFullName).collect(Collectors.joining(", ")));
                emailData.put("roomCode", room.getRoomCode());
                emailData.put("terminateDate", overdueContract.getEndDate().toString());
                emailData.put("cc", landlord.getEmail());
                emailData.put("subject", subject);

                emailService.sendEmailContract(templateName, emailData, emailTo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
