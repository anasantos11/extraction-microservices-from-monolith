package br.com.pucminas.businesslogics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.internal.WhiteboxImpl;

import br.com.pucminas.domain.Method;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

public class ServiceBLIntegrationTest {
    private static final String JAR_FILE_PATH = "src/test/resources/kanleitos-0.0.1-SNAPSHOT.jar";
    private static final String INCLUDED_PACKAGE = "br.com.kanleitos";
    private static final String REPOSITORY_PATH_NAME = "target/repos/kanleitos-API";
    private static final String REPOSITORY_URI = "https://github.com/anasantos11/kanleitos-API.git";

    private GitRepository gitRepository;
    private CallGraphRepository callGraphRepository;
    private ServiceBL serviceBL;

    @Before
    public void init() {
        File folder = new File(REPOSITORY_PATH_NAME);
        Git git = null;

        try {
            git = Git.open(folder);
        } catch (IOException e) {
            try {
                git = Git.cloneRepository().setURI(REPOSITORY_URI).setDirectory(folder).call();
            } catch (GitAPIException e1) {
                assertTrue("Error load repository", false);
            }
        }

        gitRepository = new GitRepository(git);
        callGraphRepository = new CallGraphRepository(new File(JAR_FILE_PATH), Arrays.asList(INCLUDED_PACKAGE));
        serviceBL = new ServiceBL(gitRepository, callGraphRepository, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
    }

    @Test
    public void getCandidateServices() throws Exception {
        List<Method> results = WhiteboxImpl.invokeMethod(this.serviceBL, "getCandidateServices");
        results = results.stream().filter(candidateService -> candidateService.isCandidateService())
                .collect(Collectors.toList());
        List<String> expectedResults = Arrays.asList("br.com.kanleitos.configurations.SwaggerConfig:api()",
                "br.com.kanleitos.controllers.AlaController:listarAlas(boolean)",
                "br.com.kanleitos.controllers.AlaController:alteraStatus(java.lang.Long)",
                "br.com.kanleitos.controllers.DiagnosticoController:listarDiagnosticos()",
                "br.com.kanleitos.controllers.EnfermariaController:listarEnfermarias(boolean)",
                "br.com.kanleitos.controllers.EnfermariaController:listarEnfermariasByAlas(java.lang.Long,boolean)",
                "br.com.kanleitos.controllers.EnfermariaController:alteraStatus(java.lang.Long)",
                "br.com.kanleitos.controllers.FuncionarioController:cadastrarFuncionario(br.com.kanleitos.models.Funcionario,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.FuncionarioController:getFuncionariosByStatus(boolean)",
                "br.com.kanleitos.controllers.FuncionarioController:getMedicos()",
                "br.com.kanleitos.controllers.FuncionarioController:getFuncionariosNaoMedicos()",
                "br.com.kanleitos.controllers.FuncionarioController:atualizarFuncionario(br.com.kanleitos.models.Funcionario,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.FuncionarioController:alteraStatus(java.lang.Long)",
                "br.com.kanleitos.controllers.FuncionarioController:lambda$persistFuncionario$0(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.controllers.HospitalController:getHospitals()",
                "br.com.kanleitos.controllers.HospitalController:cadastrarHospital(br.com.kanleitos.models.Hospital,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.HospitalController:updateHospital(java.lang.Integer,br.com.kanleitos.models.Hospital,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.HospitalController:inativarHospital(java.lang.Integer)",
                "br.com.kanleitos.controllers.HospitalController:ativarHospital(java.lang.Integer)",
                "br.com.kanleitos.controllers.HospitalController:lambda$updateHospital$1(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.controllers.HospitalController:lambda$cadastrarHospital$0(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.controllers.IsolamentoController:getIsolamentos()",
                "br.com.kanleitos.controllers.IsolamentoController:pedidosConcluidos(java.lang.Long)",
                "br.com.kanleitos.controllers.LeitoController:listarLeitos(boolean)",
                "br.com.kanleitos.controllers.LeitoController:getLeitosByEnfermaria(java.lang.Long,boolean)",
                "br.com.kanleitos.controllers.LeitoController:updateLeito(br.com.kanleitos.models.Leito)",
                "br.com.kanleitos.controllers.ObservacaoInternacaoController:cadastrarObservacaoInternacao(br.com.kanleitos.models.ObservacaoInternacao,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.ObservacaoInternacaoController:getObservacaoInternacaoByRegistroInternacao(long)",
                "br.com.kanleitos.controllers.ObservacaoInternacaoController:updateObservacaoInternacao(br.com.kanleitos.models.ObservacaoInternacao,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.ObservacaoInternacaoController:deletarObservacaoInternacao(java.lang.Long)",
                "br.com.kanleitos.controllers.ObservacaoInternacaoController:lambda$persistObservacaoInternacao$0(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.controllers.PacienteController:cadastrarPaciente(br.com.kanleitos.models.Paciente)",
                "br.com.kanleitos.controllers.PacienteController:listarPacientes()",
                "br.com.kanleitos.controllers.PacienteController:getPaciente(java.lang.Long,java.lang.String)",
                "br.com.kanleitos.controllers.PacienteController:listarPacientesByEnfermaria(java.lang.Long)",
                "br.com.kanleitos.controllers.PedidoInternacaoController:pedidoInternacao(br.com.kanleitos.models.PedidoInternacao)",
                "br.com.kanleitos.controllers.PedidoInternacaoController:getPaciente(java.lang.Long)",
                "br.com.kanleitos.controllers.PedidoInternacaoController:listarPedidos()",
                "br.com.kanleitos.controllers.PedidoInternacaoController:pedidosEmAndamento(br.com.kanleitos.models.Filtro)",
                "br.com.kanleitos.controllers.PendenciaInternacaoController:cadastrarPendenciaInternacao(br.com.kanleitos.models.PendenciaInternacao,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.PendenciaInternacaoController:getPendenciaInternacaoByRegistroInternacao(long)",
                "br.com.kanleitos.controllers.PendenciaInternacaoController:updatePendenciaInternacao(br.com.kanleitos.models.PendenciaInternacao,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.PendenciaInternacaoController:deletarPendenciaInternacao(java.lang.Long)",
                "br.com.kanleitos.controllers.PendenciaInternacaoController:lambda$persistPendenciaInternacao$0(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.controllers.RegistroInternacaoController:registroInternacao(br.com.kanleitos.models.RegistroInternacao)",
                "br.com.kanleitos.controllers.RegistroInternacaoController:listarInternacoes(br.com.kanleitos.models.Filtro)",
                "br.com.kanleitos.controllers.RegistroInternacaoController:encerrarInternacao(br.com.kanleitos.models.FinalizarInternacao,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.RegistroInternacaoController:lambda$encerrarInternacao$0(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:taxaPorGenero()",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:taxaPorIdade()",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:taxaPorAla(java.lang.Long)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:taxaStatusLeito(java.lang.Long)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:taxaPorTipoPendenciaInternacaoEmAndamento(java.lang.Long)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:tempoMedioAno(java.lang.Long,int)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:lambda$setDadosLeitos$6(br.com.kanleitos.models.Enfermaria,br.com.kanleitos.models.Leito)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:lambda$setDadosLeitos$5(br.com.kanleitos.models.Enfermaria,br.com.kanleitos.models.Leito)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:lambda$setDadosLeitos$4(br.com.kanleitos.models.Enfermaria,br.com.kanleitos.models.Leito)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:lambda$taxaPorAla$3(java.util.List,br.com.kanleitos.util.ResponseTaxa,br.com.kanleitos.models.Enfermaria)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:lambda$taxaPorAla$2(java.util.Set,br.com.kanleitos.models.Leito)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:lambda$filtraTaxasPorIdade$1(int,int,br.com.kanleitos.models.RegistroInternacao)",
                "br.com.kanleitos.controllers.TaxaOcupacaoController:lambda$taxaPorGenero$0(br.com.kanleitos.models.RegistroInternacao)",
                "br.com.kanleitos.controllers.TipoPendenciaController:cadastrarTipo(br.com.kanleitos.models.TipoPendencia,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.TipoPendenciaController:getPendenciasByStatus(boolean)",
                "br.com.kanleitos.controllers.TipoPendenciaController:atualizarTipo(br.com.kanleitos.models.TipoPendencia,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.TipoPendenciaController:alteraStatus(java.lang.Integer)",
                "br.com.kanleitos.controllers.TipoPendenciaController:lambda$persistTipoPendencia$0(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.controllers.TransferenciaLeitoController:cadastrarTransferencia(br.com.kanleitos.models.TransferenciaLeito,org.springframework.validation.BindingResult)",
                "br.com.kanleitos.controllers.TransferenciaLeitoController:getTransferenciasByRegistroInternacao(long)",
                "br.com.kanleitos.controllers.TransferenciaLeitoController:lambda$persistTransferenciaLeito$0(br.com.kanleitos.util.Response,org.springframework.validation.ObjectError)",
                "br.com.kanleitos.dao.TaxaOcupacaoDao:lambda$getTaxaTempoMedioAno$1(java.util.ArrayList,java.sql.ResultSet)",
                "br.com.kanleitos.models.enums.RotuloTaxaOcupacao:values()",
                "br.com.kanleitos.models.enums.StatusPedido:values()",
                "br.com.kanleitos.models.enums.StatusRegistro:values()",
                "br.com.kanleitos.models.enums.TipoStatusLeito:values()",
                "br.com.kanleitos.validators.FinalizarValidator:validate(java.lang.Object,org.springframework.validation.Errors)",
                "br.com.kanleitos.validators.FuncionarioValidator:validate(java.lang.Object,org.springframework.validation.Errors)",
                "br.com.kanleitos.validators.HospitalValidator:validate(java.lang.Object,org.springframework.validation.Errors)",
                "br.com.kanleitos.validators.IsolamentoValidator:validate(java.lang.Object,org.springframework.validation.Errors)",
                "br.com.kanleitos.validators.ObservacaoInternacaoValidator:validate(java.lang.Object,org.springframework.validation.Errors)",
                "br.com.kanleitos.validators.PendenciaInternacaoValidator:validate(java.lang.Object,org.springframework.validation.Errors)",
                "br.com.kanleitos.validators.TipoPendenciaValidator:validate(java.lang.Object,org.springframework.validation.Errors)",
                "br.com.kanleitos.validators.TransferenciaLeitoValidator:validate(java.lang.Object,org.springframework.validation.Errors)");

        List<String> unexpectedResults = results.stream()
                .filter(result -> !expectedResults.contains(result.getFullMethodName())).map(Method::getFullMethodName)
                .collect(Collectors.toList());
        assertTrue("Unexpected results list is not empty", unexpectedResults.isEmpty());
        assertEquals(82, results.size());
    }

    @Test
    public void generateSimilarityMatrix() throws IOException, GitAPIException {
        Map<String, Set<Method>> microservices = serviceBL.groupServicesInMicroservices();
        assertNotNull(microservices);
    }
}