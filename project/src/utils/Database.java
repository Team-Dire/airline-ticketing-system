package src.utils;

import src.models.*;
import src.utils.types.ClasseAssentos;
import src.utils.types.Recorrencias;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Database {
    private Connection connection;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            this.seed();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void seed() {
        this.createTables();
    }

    public boolean execute(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String[]> query(String sql) {
        ArrayList<String[]> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String[] row = new String[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < row.length; i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void createTables() {
        String queryVoosProgramados = String.format("""
                        CREATE TABLE IF NOT EXISTS voosProgramados (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            aeroportoOrigem VARCHAR(4) NOT NULL CHECK (LENGTH(aeroportoOrigem) = 3 OR LENGTH(aeroportoOrigem) = 4),
                            aeroportoDestino VARCHAR(4) NOT NULL CHECK (LENGTH(aeroportoDestino) = 3 OR LENGTH(aeroportoDestino) = 4),
                            aviao VARCHAR(255) NOT NULL,
                            horarioPrevisto DATETIME NOT NULL CHECK (horarioPrevisto > CURRENT_TIMESTAMP),
                            recorrencia VARCHAR(10) CHECK (recorrencia IN ('DIÁRIO', 'SEMANAL', 'MENSAL', 'ÚNICO')),
                            vagasEconomica INTEGER NOT NULL CHECK (vagasEconomica >= 0),
                            vagasExecutiva INTEGER NOT NULL CHECK (vagasExecutiva >= 0),
                            vagasPrimeira INTEGER NOT NULL CHECK (vagasPrimeira >= 0)
                        )
                        """);
        this.execute(queryVoosProgramados);

        String queryVoos = String.format("""
                        CREATE TABLE IF NOT EXISTS voos (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            vooProgramadoId INTEGER NOT NULL,
                            horarioPrevisto DATETIME NOT NULL CHECK (horarioPrevisto > CURRENT_TIMESTAMP),
                            horarioPartida DATETIME,
                            FOREIGN KEY (vooProgramadoId) REFERENCES voosProgramados(id)
                        )
                        """);
        this.execute(queryVoos);

        String queryClasses = String.format("""
                        CREATE TABLE IF NOT EXISTS classes (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            vooId INTEGER NOT NULL,
                            tipoClasse VARCHAR(10) NOT NULL CHECK (tipoClasse IN ('ECONÔMICA', 'EXECUTIVA', 'PRIMEIRA_CLASSE')),
                            FOREIGN KEY (vooId) REFERENCES voos(id)
                        )
                        """);
        this.execute(queryClasses);

        String queryVagas = String.format("""
                        CREATE TABLE IF NOT EXISTS vagas (
                            classeId INTEGER NOT NULL,
                            assento VARCHAR(5) NOT NULL,
                            ocupada BOOLEAN NOT NULL DEFAULT 0,
                            nomeCompleto VARCHAR(255),
                            dataNascimento DATETIME,
                            numId VARCHAR(255),
                            tipoNumId VARCHAR(255),
                            FOREIGN KEY (classeId) REFERENCES classes(id)
                            PRIMARY KEY (classeId, assento)
                        )
                        """);
        this.execute(queryVagas);
    }



    public VooProgramado armazenarVooProgramado(VooProgramado vooProgramado) {
        String query = String.format("""
                        INSERT INTO voosProgramados (
                            aeroportoOrigem,
                            aeroportoDestino,
                            aviao,
                            horarioPrevisto,
                            recorrencia,
                            vagasEconomica, 
                            vagasExecutiva,
                            vagasPrimeira
                        ) VALUES (
                            '%s',
                            '%s',
                            '%s',
                            '%s',
                            '%s',
                            %d,
                            %d,
                            %d
                        )
                        """,
                vooProgramado.getAeroportoOrigem(),
                vooProgramado.getAeroportoDestino(),
                vooProgramado.getAviao(),
                vooProgramado.getHorarioPrevisto(),
                vooProgramado.getRecorrencia().toString(),
                vooProgramado.getVagasEconomica(),
                vooProgramado.getVagasExecutiva(),
                vooProgramado.getVagasPrimeiraClasse()
        );
        boolean success = execute(query);
        if (success) {
            vooProgramado.setId(this.getLastId("voosProgramados"));
            return vooProgramado;
        }
        return null;
    }

    private int getLastId(String table) {
        String query = String.format("SELECT MAX(id) FROM %s", table);
        ArrayList<String[]> result = this.query(query);
        return Integer.parseInt(result.get(0)[0]);
    }


    public boolean criarVooDesdeProgramado(VooProgramado vooProgramado) {
        int sumDays = 0;
        if (vooProgramado.getRecorrencia() == Recorrencias.DIÁRIO) {
            sumDays = 1;
        } else if (vooProgramado.getRecorrencia() == Recorrencias.SEMANAL) {
            sumDays = 7;
        } else if (vooProgramado.getRecorrencia() == Recorrencias.MENSAL) {
            sumDays = 30;
        } else if (vooProgramado.getRecorrencia() == Recorrencias.ÚNICO) {
            sumDays = 0;
        }

        ArrayList<LocalDateTime> horariosToBeCreated = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime curDate = vooProgramado.getHorarioPrevisto();

        if (sumDays != 0) {
            int maxDaysFromNow = 31;
            while (curDate.isBefore(now)) {
                curDate = curDate.plusDays(sumDays);
            }
            while (curDate.isBefore(now.plusDays(maxDaysFromNow))) {
                horariosToBeCreated.add(curDate);
                curDate = curDate.plusDays(sumDays);
            }
        }
        else if (curDate.isAfter(now)) {
            horariosToBeCreated.add(curDate);
        }


        for (LocalDateTime horario : horariosToBeCreated) {
            String queryGetExistingFlight = String.format("""
                            SELECT * FROM voos JOIN voosProgramados as vp ON vp.id = voos.vooProgramadoId
                            WHERE vp.id = '%s' AND vp.aeroportoOrigem = '%s' AND vp.aeroportoDestino = '%s' AND vp.aviao = '%s' AND vp.recorrencia = '%s' AND voos.horarioPrevisto = '%s'
                            """,
                    vooProgramado.getId(),
                    vooProgramado.getAeroportoOrigem(),
                    vooProgramado.getAeroportoDestino(),
                    vooProgramado.getAviao(),
                    vooProgramado.getRecorrencia().toString(),
                    horario
            );

            ArrayList<String[]> result = this.query(queryGetExistingFlight);

            if (result.size() != 0) {
                continue;
            }

            String query = String.format("""
                            INSERT INTO voos (
                                vooProgramadoId,
                                horarioPrevisto
                            ) VALUES (
                                %d,
                                '%s'
                            )
                            """,
                    vooProgramado.getId(),
                    horario
            );
            execute(query);


            String queryVooId = String.format("""
                            SELECT id FROM voos WHERE vooProgramadoId = %d AND horarioPrevisto = '%s'
                            """,
                    vooProgramado.getId(),
                    horario
            );

            ArrayList<String[]> resultVooId = this.query(queryVooId);


            int vooId = Integer.parseInt(resultVooId.get(0)[0]);

            ArrayList<Classe> classes = new ArrayList<>();
            int vagasEconomica = vooProgramado.getVagasEconomica();
            classes.add(new Classe(ClasseAssentos.ECONÔMICA, vagasEconomica));
            int vagasExecutiva = vooProgramado.getVagasExecutiva();
            classes.add(new Classe(ClasseAssentos.EXECUTIVA, vagasExecutiva));
            int vagasPrimeiraClasse = vooProgramado.getVagasPrimeiraClasse();
            classes.add(new Classe(ClasseAssentos.PRIMEIRA_CLASSE, vagasPrimeiraClasse));


            for (Classe classe : classes) {
                String queryClasse = String.format("""
                                INSERT INTO classes (
                                    vooId,
                                    tipoClasse
                                ) VALUES (
                                    %d,
                                    '%s'
                                )
                                """,
                        vooId,
                        classe.getTipoClasse().toString()
                );
                execute(queryClasse);


                String queryClasseId = String.format("""
                                SELECT id FROM classes WHERE vooId = %d AND tipoClasse = '%s'
                                """,
                        vooId,
                        classe.getTipoClasse().toString()
                );


                ArrayList<String[]> resultClasseId = this.query(queryClasseId);
                int classeId = Integer.parseInt(resultClasseId.get(0)[0]);

                for (Vaga vaga : classe.getVagas()) {
                    Passageiro passageiro = vaga.getPassageiro();
                    String queryVaga = String.format("""
                                    INSERT INTO vagas (classeId, assento) VALUES (%d, '%s')
                                    """,
                            classeId,
                            vaga.getAssento()
                    );
                    execute(queryVaga);
                }

            }
        }
        return true;
    }

    public ArrayList<VooProgramado> obterVoosProgramados() {
        ArrayList<String[]> flights = query("SELECT * FROM voosProgramados");
        ArrayList<VooProgramado> voosProgramados = new ArrayList<>();

        for (String[] flight : flights) {
            VooProgramado vooProgramado = new VooProgramado(
                    Integer.parseInt(flight[0]),
                    flight[1],
                    flight[2],
                    flight[3],
                    LocalDateTime.parse(flight[4]),
                    Recorrencias.valueOf(flight[5]),
                    Integer.parseInt(flight[6]),
                    Integer.parseInt(flight[7]),
                    Integer.parseInt(flight[8])
            );
            voosProgramados.add(vooProgramado);
        }
        return voosProgramados;
    }

    public void criarVoosDesdeVoosProgramados(ArrayList<VooProgramado> voosProgramados) {
        for (VooProgramado vooProgramado : voosProgramados) {
            criarVooDesdeProgramado(vooProgramado);
        }
    }

    public boolean atualizarVaga(Classe classe, Vaga vaga) {
        Passageiro passageiro = vaga.getPassageiro();
        String query = String.format("""
                        UPDATE vagas SET ocupada = %d, nomeCompleto = '%s', dataNascimento = '%s', numId = '%s', tipoNumId = '%s' WHERE classeId = %d AND assento = '%s'
                        """,
                passageiro != null ? 1 : 0,
                passageiro != null ? passageiro.getNomeCompleto() : "",
                passageiro != null ? passageiro.getDataNascimento().toString() : "",
                passageiro != null ? passageiro.getNumId() : "",
                passageiro != null ? passageiro.getTipoNumId().toString() : "",
                classe.getId(),
                vaga.getAssento()
        );
        return execute(query);
    }

    public ArrayList<Voo> obterVoos() {
        String query = String.format("""
                SELECT * FROM voos JOIN voosProgramados ON voos.vooProgramadoId = voosProgramados.id
                """);
        ArrayList<String[]> flights = query(query);
        ArrayList<Voo> voos = new ArrayList<>();

        for (String[] flight : flights) {
            int voosId = Integer.parseInt(flight[1]);
            String queryClasses = String.format("""
                    SELECT * FROM classes WHERE vooId = %d
                    """, voosId);
            ArrayList<String[]> classes = query(queryClasses);
            ArrayList<Classe> classesVoo = new ArrayList<>();

            for (String[] classe : classes) {
                int classeId = Integer.parseInt(classe[0]);
                String queryVagas = String.format("""
                        SELECT * FROM vagas WHERE classeId = %d
                        """, classeId);
                ArrayList<String[]> vagas = query(queryVagas);
                ArrayList<Vaga> vagasClasse = new ArrayList<>();

                for (String[] vaga : vagas) {
//                    for (int i = 0; i < vaga.length; i++) {
//                        System.out.println("Field " + i + ": " + vaga[i]);
//                    }
                    String assento = vaga[1];
                    boolean ocupada = vaga[2].equals("1");
                    String nomeCompleto = vaga[3];
                    LocalDate dataNascimento = vaga[4] == null ? null : LocalDate.parse(vaga[4]);
                    String numId = vaga[5];
                    String tipoNumId = vaga[6];
                    Passageiro passageiro = new Passageiro(nomeCompleto, numId, tipoNumId, dataNascimento);
                    Vaga vagaClasse = new Vaga(assento, ocupada, passageiro);
                    vagasClasse.add(vagaClasse);
                }
                int id = Integer.parseInt(classe[0]);
                String tipoClasse = classe[2];
                Classe classeVoo = new Classe(id, tipoClasse, vagasClasse);
                classesVoo.add(classeVoo);
            }

//            for (int i = 0; i < flight.length; i++) {
//                System.out.println("Field " + i + ": " + flight[i]);
//            }

            int vooId = Integer.parseInt(flight[0]);
            int vooProgramadoId = Integer.parseInt(flight[1]);
            LocalDateTime horarioPrevisto = LocalDateTime.parse(flight[2]);
            LocalDateTime horarioPartida = flight[3] == null ? null : LocalDateTime.parse(flight[3]);
            String joinKey = flight[4];
            String aeroportoOrigem = flight[5];
            String aeroportoDestino = flight[6];
            String aviao = flight[7];
            LocalDateTime horarioPrevisto_ = LocalDateTime.parse(flight[8]);
            Recorrencias recorrencia = Recorrencias.valueOf(flight[9]);
            int vagasEconomica = Integer.parseInt(flight[10]);
            int vagasExecutiva = Integer.parseInt(flight[11]);
            int vagasPrimeiraClasse = Integer.parseInt(flight[12]);
            Voo voo = new Voo(vooId, vooProgramadoId, aeroportoOrigem, aeroportoDestino, aviao, horarioPrevisto, horarioPartida, recorrencia, classesVoo);
            voos.add(voo);
        }
        return voos;
    }
}

