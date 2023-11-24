package com.ifsul.lawbot.util;

import com.ifsul.lawbot.entities.Autor;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.entities.Reu;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.AutorRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import com.ifsul.lawbot.repositories.ReuRepository;
import com.ifsul.lawbot.services.GerarChaveService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

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

    @Autowired
    private GerarChaveService gerarChaveService;

    public void leArquivo(MultipartFile file, Long id){
        var advogado = advogadoRepository.getReferenceById(id);

        try{
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row : sheet){
                Chave key = gerarChaveService.findKey();
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
                                        String cleanString = text.replaceAll("[.,()-]", "");
                                        String resultado = cleanString.substring(0, Math.min(cleanString.length(), 20));
                                        processo.setNumeroProcesso(encriptar(resultado, key.getChavePublica()));
                                    }
                                    else {
                                        processo.setNumeroProcesso(null);
                                    }
                                    break;
                                case 1:
                                    // Classe
                                    processo.setClasse(encriptar(cell.toString(), key.getChavePublica()));
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
                                            nomesAutores[i].setNome(encriptar(autores[i], key.getChavePublica()));
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
                                           nomesReus[i].setNome(encriptar(reus[i], key.getChavePublica()));
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
                                    processo.setLocalidade(encriptar(cell.toString(), key.getChavePublica()));
                                    break;
                                case 5:
                                    // Assunto
                                    processo.setAssunto(encriptar(cell.toString(), key.getChavePublica()));
                                    break;
                                case 6:
                                    // Ultimo evento
                                    processo.setUltimoEvento(encriptar(cell.toString(), key.getChavePublica()));
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
                        processos.get(i).setChave(key);
                        processos.get(i).setClienteDefinido(false);
                        processos.get(i).setAdvogado(advogado);
                        advogado.getProcessos().add(processos.get(i));
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
