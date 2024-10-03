package org.example.springbootproject.scheduled;

import org.example.springbootproject.entity.Bill;
import org.example.springbootproject.entity.Room;
import org.example.springbootproject.service.BillService;
import org.example.springbootproject.service.EmailService;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BillDailyTask extends BaseTask {

    @Autowired
    private BillService billService;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "${task.dailyCron}")
    public void runDailyTaskAtMidnight() {
        Map<String, List<Bill>> listData = billService.handleCheckBillDaily();
        List<Bill> listUnpaidBills = listData.get("listUnpaidBills");
        handleBills(listUnpaidBills, "bill_overdue_template_en");
    }

    public void handleBills(List<Bill> listBills, String templateName) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            for (Bill overdueBill : listBills) {
                Room room = overdueBill.getRoom();
                Map<String, Object> emailData = new HashMap<>();
                emailData.put("tenants", room.getRoomsTenants());
                emailData.put("roomCode", room.getRoomCode());
                emailData.put("date", dateFormat.format(overdueBill.getMonth()));
                emailData.put("overdue", Constants.OVERDUE_BILL_DATE);

                emailService.sendEmailBillOverdue(templateName, emailData);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
