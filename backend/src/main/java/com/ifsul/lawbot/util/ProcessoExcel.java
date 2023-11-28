package com.ifsul.lawbot.util;

import com.ifsul.lawbot.dto.processo.EditarProcessoRequest;
import com.ifsul.lawbot.entities.*;
import com.ifsul.lawbot.repositories.*;
import com.ifsul.lawbot.services.GerarChaveService;
import com.ifsul.lawbot.services.ProcessoService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;
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

    @Autowired
    private ProcessoService service;

    @Autowired
    private HistoricoRepository historicoRepository;

    public void leArquivo(MultipartFile file, Long id){
        var advogado = advogadoRepository.getReferenceById(id);

        try{
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<Processo> processos = new ArrayList<>();
            for(Row row : sheet){
                Chave key = gerarChaveService.findKey();
                Processo processo = new Processo();
                if (row.getRowNum() > 1){
                    for (Cell cell : row){
                        if (!cell.toString().equals("") && !cell.toString().equals(" ")){
                            switch (cell.getColumnIndex()) {
                                case 0:
                                    // Numero do processo
                                    String text = cell.toString();
                                    String cleanString = text.replaceAll("[.,()-]", "");
                                    String resultado = cleanString.substring(0, Math.min(cleanString.length(), 20));
                                    processo.setNumeroProcesso(encriptar(resultado, key.getChavePublica()));
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

                        }
                    }
                    processo.setChave(key);
                    processos.add(processo);
                }

                workbook.close();
            }

            for (Processo value : processos) {
                value.setClienteDefinido(false);
                value.setAdvogado(advogado);
                advogado.getProcessos().add(value);

                var p = confereProcesso(decriptar(value.getNumeroProcesso(), value.getChave().getChavePrivada()));

                if (p >= 0) {
                    var pro = processoRepository.findById(p).get();
                    // O processo já existe, então criamos uma nova etapa no histórico
                    Historico historico = new Historico();
                    historico.setProcesso(pro); // Usamos o processo existente
                    historico.setDataAtualizacao(value.getDataAtualizacao());
                    historico.setUltimaAtualizacao(value.getUltimoEvento());

                    if(verificaHistorico(historico, pro)){
                        System.out.println("Editou!");

                        historicoRepository.save(historico);

                        // Atualizar informações no processo, se necessário
                        pro.setDataAtualizacao(historico.getDataAtualizacao());
                        pro.setUltimoEvento(historico.getUltimaAtualizacao());
                        processoRepository.save(pro);
                    }
                } else {
                    System.out.println("Salvou!");
                    // O processo não existe, então criamos um novo processo e um novo histórico
                    processoRepository.save(value);

                    Historico historico = new Historico();
                    historico.setProcesso(value);
                    historico.setDataAtualizacao(value.getDataAtualizacao());
                    historico.setUltimaAtualizacao(value.getUltimoEvento());
                    historicoRepository.save(historico);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public Long confereProcesso(String processo){
        List<Processo> p = processoRepository.findAll();
        for(Processo pro : p){
            var n1 = processo;
            var n2 = decriptar(pro.getNumeroProcesso(), pro.getChave().getChavePrivada());
            if(n1.equals(n2)){
                return pro.getId();
            }
        }
        return -1L;
    }

    public LocalDate converteData(String dataString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-u");
        LocalDate localDate = LocalDate.parse(dataString, formatter);

        return localDate;
    }

    private boolean verificaHistorico(Historico historico, Processo processo){
        List<Historico> historicos = historicoRepository.findByProcesso_IdOrderByDataAtualizacaoDesc(processo.getId());

        var t1 = decriptar(historicos.get(0).getUltimaAtualizacao(), processo.getChave().getChavePrivada());
        var t2 = decriptar(historico.getUltimaAtualizacao(), processo.getChave().getChavePrivada());

        if(!t1.equalsIgnoreCase(t2)){
            return true;
        }
        return false;
    }
}
