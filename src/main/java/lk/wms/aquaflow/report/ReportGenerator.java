package lk.wms.aquaflow.report;

import lk.wms.aquaflow.db.DBConnection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {
    public static void generateHouseholdReport() {
        try {
            JasperReport report = JasperCompileManager.compileReport(
                    ReportGenerator.class.getResourceAsStream("/lk/wms/aquaflow/reports/household.jrxml")
            );
            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_DATE", LocalDate.now().toString());
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    report,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            // Consider throwing a custom exception or using a logger
        }
    }
}