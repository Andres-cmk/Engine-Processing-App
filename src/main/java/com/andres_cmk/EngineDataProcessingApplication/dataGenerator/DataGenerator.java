package com.andres_cmk.EngineDataProcessingApplication.dataGenerator;

import net.datafaker.Faker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DataGenerator {
    public static void main(String[] args) {

        System.out.println("🚀 Iniciando generación de datos...");


        Faker faker = new Faker(new Locale.Builder().setLanguage("es").build());

        String nameFile = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "datos_bancarios.csv";

        int numRows = 1_000_000;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nameFile))) {


            writer.write("idTransaccion,fecha,monto,moneda,comercio,tipo,cuentaOrigen,bancoDestino,nombreResponsable\n");

            for (int i = 0; i < numRows; i++) {

                // --- GENERACIÓN DE CAMPOS ---

                String id = faker.internet().uuid();


                String fecha = faker.timeAndDate().past(30, TimeUnit.DAYS, "yyyy-MM-dd HH:mm:ss");


                String monto = String.format("%.2f", faker.number().randomDouble(2, 10, 10000)).replace(",", ".");

                String moneda = faker.money().currencySymbol();


                String comercio = "\"" + faker.company().name() + "\"";

                String tipo = faker.options().option("COMPRA", "RETIRO", "TRANSFERENCIA");
                String cuenta = faker.finance().iban();
                String banco = "\"" + faker.company().name() + "\"";
                String responsable = "\"" + faker.name().fullName() + "\"";


                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        id, fecha, monto, moneda, comercio, tipo, cuenta, banco, responsable);

                writer.write(line);


                if (i % 5000 == 0) System.out.println("... Generadas: " + i);
            }

            System.out.println("✅ ¡ÉXITO! Archivo generado en: " + nameFile);

        } catch (IOException e) {
            System.err.println("❌ Error escribiendo el archivo: " + e.getMessage());
        }
    }
}