package com.ifsul.lawbot.util;

import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessoExcel {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    public void leArquivo(MultipartFile file){
        System.out.println("Le arquivo...");
        try{
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Try ");
            for(Row row : sheet){
                System.out.println("Row " + row.getRowNum());
                List<Processo> processos = new ArrayList<>();
                Processo processo = new Processo();
                if (row.getRowNum() > 1){
                    for (Cell cell : row){
                        if (cell.toString() != "" && cell.toString() != " "){
                            switch (cell.getColumnIndex()) {
                                case 0:
                                    // Numero do processo
                                    if (confereProcesso(cell.toString())){
                                        String text = cell.toString();
                                        String resultado = text.replaceAll("[a-zA-Z.\\s]", "");
                                        processo.setNumeroProcesso(resultado);
                                    }
                                    break;
                                case 1:
                                    // Classe
                                    processo.setClasse(cell.toString());
                                    break;
                                case 2:
                                    // Autores
                                    RichTextString autor = cell.getRichStringCellValue();
                                    String[] autores = autor.getString().split("\n");
                                    for(int i = 0; i < autores.length; i++){
                                        processo.getNomeAutor().add(autores[i]);
                                    }
                                    break;
                                case 3:
                                    // Reus
                                    RichTextString reu = cell.getRichStringCellValue();
                                    String[] reus = reu.getString().split("\n");
                                    for(int i = 0; i < reus.length; i++){
                                        processo.getNomeReu().add(reus[i]);
                                    }
                                    break;
                                case 4:
                                    // Localidade
                                    processo.setLocalidade(cell.toString());
                                    break;
                                case 5:
                                    // Assunto
                                    processo.setAssunto(cell.toString());
                                    break;
                                case 6:
                                    // Ultimo evento
                                    processo.setUltimoEvento(cell.toString());
                                    break;
                                case 7:
                                    // Data
                                    processo.setDataAtualizacao(converteData(cell.toString()));
                                    break;
                                default:
                                    // Código para o caso padrão (se não corresponder a nenhum dos casos anteriores)
                                    break;
                            }

                        }
                    }
                }
                if (processo.getNumeroProcesso() != null){
                    for (int i = 0; i < processos.size(); i++){
                        processoRepository.save(processos.get(i));
                    }
                }
                workbook.close();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public boolean confereProcesso(String processo){
        Processo p = processoRepository.findByNumeroProcesso(processo);

        try {
            if ( p == null ){
                return true;
            } else {
                return false;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public LocalDate converteData(String dataString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-u");
        LocalDate localDate = LocalDate.parse(dataString, formatter);

        return localDate;
    }
}
