package com.ifsul.lawbot.util;

import com.ifsul.lawbot.entities.Autor;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.entities.Reu;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.AutorRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import com.ifsul.lawbot.repositories.ReuRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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
    private AutorRepository autorRepository;
    @Autowired
    private ReuRepository reuRepository;
    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    public void leArquivo(MultipartFile file){
        try{
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row : sheet){
                Processo processo = new Processo();
                List<Processo> processos = new ArrayList<>();
                if (row.getRowNum() > 1){
                    for (Cell cell : row){
                        if (!cell.toString().equals("") && !cell.toString().equals(" ")){
                            switch (cell.getColumnIndex()) {
                                case 0:
                                    // Numero do processo
                                    if (confereProcesso(cell.toString())){
                                        String text = cell.toString();
                                        String resultado = text.replaceAll("[a-zA-Z.\\s]", "");
                                        processo.setNumeroProcesso(resultado);
                                    }
                                    else {
                                        processo.setNumeroProcesso(null);
                                    }
                                    break;
                                case 1:
                                    // Classe
                                    processo.setClasse(cell.toString());
                                    break;
                                case 2:
                                    // Autores
                                    String autor = cell.getStringCellValue();
                                    String[] autores = autor.split("\n");
                                    int tamanho = 1;
                                    if(autores.length > tamanho){
                                        tamanho = autores.length;
                                    }
                                    Autor[] nomesAutores = new Autor[tamanho];
                                    for(int i = 0; i < tamanho; i++){
                                        nomesAutores[i] = new Autor();
                                        try{
                                            nomesAutores[i].setNome(autores[i]);
                                        }catch(Exception ex){
                                            nomesAutores[i].setNome(null);
                                        }
                                    }
                                    for(int i = 0; i < tamanho; i++){
                                        nomesAutores[i].setProcesso(processo);
                                        processo.getNomeAutor().add(nomesAutores[i]);
                                    }
                                    break;
                                case 3:
                                    // Reus
                                    String reu = cell.getStringCellValue();
                                    String[] reus = reu.split("\n");
                                    int tam = 1;
                                    if(reus.length > tam){
                                        tam = reus.length;
                                    }
                                    Reu[] nomesReus = new Reu[tam];
                                    for(int i = 0; i < tam; i++){
                                        nomesReus[i] = new Reu();
                                        try{
                                           nomesReus[i].setNome(reus[i]);
                                       }catch(Exception ex){
                                           nomesReus[i].setNome(null);
                                       }
                                    }
                                    for(int i = 0; i < tam; i++){
                                        nomesReus[i].setProcesso(processo);
                                        processo.getNomeReu().add(nomesReus[i]);
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
                            processos.add(processo);

                        }
                    }
                }
                if (processo.getNumeroProcesso() != null){
                    for (int i = 0; i < processos.size(); i++){
                        processoRepository.save(processos.get(i));
                        for(int j = 0; j < processos.get(i).getNomeAutor().size(); j++){
                            autorRepository.save(processos.get(j).getNomeAutor().get(j));
                        }
                        for(int j = 0; j < processos.get(i).getNomeReu().size(); j++){
                            reuRepository.save(processos.get(i).getNomeReu().get(j));
                        }
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
