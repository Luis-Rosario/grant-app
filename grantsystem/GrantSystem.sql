-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Tempo de geração: 19-Dez-2020 às 18:47
-- Versão do servidor: 8.0.22
-- versão do PHP: 7.4.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `GrantSystem`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `applicationdao`
--

CREATE TABLE `applicationdao` (
  `applicationid` bigint NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `submission_date` date DEFAULT NULL,
  `grant_grant_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `applicationdao`
--

INSERT INTO `applicationdao` (`applicationid`, `status`, `submission_date`, `grant_grant_id`, `student_id`) VALUES
(1, 'SUBMITTED', '2020-11-20', 1, 2),
(2, 'DRAFT', '2020-12-03', 1, 3),
(3, 'SUBMITTED', '2020-12-25', 3, 4),
(4, 'SUBMITTED', '2021-01-12', 3, 5),
(5, 'SUBMITTED', '2020-12-29', 4, 6),
(6, 'SUBMITTED', '2021-01-20', 4, 7),
(7, 'DRAFT', '2020-12-26', 5, 8),
(8, 'SUBMITTED', '2021-01-15', 5, 9),
(52, 'NOT_GRANTED', '2020-12-17', 9, 2),
(53, 'GRANTED', '2020-12-17', 9, 5),
(54, 'GRANTED', '2020-12-17', 6, 4),
(102, 'SUBMITTED', '2020-12-19', 2, 3);

-- --------------------------------------------------------

--
-- Estrutura da tabela `applicationdao_responses`
--

CREATE TABLE `applicationdao_responses` (
  `applicationdao_applicationid` bigint NOT NULL,
  `responses_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `applicationdao_responses`
--

INSERT INTO `applicationdao_responses` (`applicationdao_applicationid`, `responses_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(3, 4),
(4, 5),
(4, 6),
(5, 7),
(5, 8),
(6, 9),
(6, 10),
(7, 11),
(7, 12),
(8, 13),
(8, 14),
(52, 52),
(53, 53),
(54, 54),
(54, 55),
(102, 102);

-- --------------------------------------------------------

--
-- Estrutura da tabela `application_seq`
--

CREATE TABLE `application_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `application_seq`
--

INSERT INTO `application_seq` (`next_val`) VALUES
(201);

-- --------------------------------------------------------

--
-- Estrutura da tabela `cvdao`
--

CREATE TABLE `cvdao` (
  `id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `cvdao`
--

INSERT INTO `cvdao` (`id`) VALUES
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9),
(10);

-- --------------------------------------------------------

--
-- Estrutura da tabela `cvdao_fields`
--

CREATE TABLE `cvdao_fields` (
  `cvdao_id` bigint NOT NULL,
  `fields_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `cv_seq`
--

CREATE TABLE `cv_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `cv_seq`
--

INSERT INTO `cv_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Estrutura da tabela `evaluationdao`
--

CREATE TABLE `evaluationdao` (
  `eval_id` bigint NOT NULL,
  `status` bit(1) NOT NULL,
  `text_field` varchar(255) DEFAULT NULL,
  `app_applicationid` bigint DEFAULT NULL,
  `grant_grant_id` bigint DEFAULT NULL,
  `rev_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `evaluationdao`
--

INSERT INTO `evaluationdao` (`eval_id`, `status`, `text_field`, `app_applicationid`, `grant_grant_id`, `rev_id`) VALUES
(1, b'0', 'Could not Review (same institution)', 1, 1, 18),
(2, b'0', 'Could not Review (same institution)', 1, 1, 17),
(3, b'0', 'Could not Review (same institution)', 3, 3, 19),
(4, b'0', 'Could not Review (same institution)', 4, 3, 19),
(5, b'0', 'Could not Review (same institution)', 5, 4, 21),
(6, b'0', 'Could not Review (same institution)', 6, 4, 21),
(7, b'0', 'Could not Review (same institution)', 8, 5, 24),
(52, b'0', 'Could not Review (same institution)', 52, 9, 18),
(53, b'1', 'Se sabe respirar debaixo de água então está aprovado!', 53, 9, 18),
(54, b'1', 'O que é preciso é fazer flexões, mas vou dar oportunidade', 53, 9, 25),
(55, b'1', 'Se sabe fazer flexões aprovado!', 52, 9, 25),
(56, b'0', 'É mais que isso!', 52, 9, 26),
(57, b'0', 'Não chega!', 53, 9, 26),
(58, b'0', 'Ser soldado é mais que isso!', 52, 9, 22),
(59, b'1', 'Vamos ver se consegue!', 53, 9, 22),
(60, b'1', 'Contratado!', 54, 6, 25),
(61, b'1', 'Se usa máscara então é bem-vindo! ', 54, 6, 24),
(62, b'1', 'Se nunca teve covid é porque é muito responsável!', 54, 6, 22),
(63, b'1', 'Gosto muito do seu sentido de responsabilidade.', 54, 6, 17),
(102, b'0', 'Could not Review (same institution)', 102, 2, 18),
(103, b'1', 'Tem muita experiência, aprovado!', 5, 4, 23),
(104, b'1', 'Se joga Atari é porque sabe muito!', 5, 4, 20);

-- --------------------------------------------------------

--
-- Estrutura da tabela `eval_seq`
--

CREATE TABLE `eval_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `eval_seq`
--

INSERT INTO `eval_seq` (`next_val`) VALUES
(201);

-- --------------------------------------------------------

--
-- Estrutura da tabela `fielddao`
--

CREATE TABLE `fielddao` (
  `id` bigint NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `mandatory` bit(1) NOT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `field_seq`
--

CREATE TABLE `field_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `field_seq`
--

INSERT INTO `field_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `grantdao`
--

CREATE TABLE `grantdao` (
  `grant_id` bigint NOT NULL,
  `deadline` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `funding` int NOT NULL,
  `opening_date` date DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `sponsor_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `grantdao`
--

INSERT INTO `grantdao` (`grant_id`, `deadline`, `description`, `funding`, `opening_date`, `title`, `sponsor_id`) VALUES
(1, '2021-01-20', 'Nesta investigaçao irá ser realizada na FCT', 20000, '2020-11-18', 'Bolsa de Investigação na area da Verificação de Software', 12),
(2, '2021-02-03', 'Com esta bolsa o aluno irá fazer parte de um projeto de carpintaria.', 80000, '2020-11-21', 'Bolsa para aprendiz de carpinteiro', 12),
(3, '2021-01-23', 'Investigação muito dificil e para pessoas com tempo', 50000, '2020-12-02', 'Investigação na area da Aprendizagem Automática', 13),
(4, '2021-01-27', 'Jogar todo o tipo de jogos, desde PC a Gameboy', 100000, '2020-11-25', 'Bolsa para sentar à frente do computador e jogar', 13),
(5, '2021-02-02', 'Nesta investigacao o aluno ira implementar um sistema de grants', 70000, '2020-12-15', 'Bolsa na area da Consecao e Implementacao de Aplicacoes na Internet', 14),
(6, '2020-12-18', 'Vamos matar este virus!', 70000, '2020-12-15', 'Participação na elaboração do Plano de Vacinação do Covid-19', 14),
(7, '2021-02-02', 'Este trabalho ira ser levado a cabo ao longo de 1 ano e necesitamos de membros para o projeto que sejam proeficientes em JS e as suas frameworks atuais', 20000, '2020-12-15', 'Desenvolvimento de uma framework de JS que visa a eficiência de desenvolvimento', 15),
(8, '2021-02-25', 'Com esta bolsa o aluno irá dividir atomos.', 80000, '2020-11-21', 'Bolsa para Divisor de Atomos', 15),
(9, '2020-12-18', 'Com esta bolsa o aluno irá fazer parte de um projeto de fuzileiro.', 80000, '2020-11-21', 'Bolsa para aprendiz de fuzileiro', 16),
(10, '2021-01-12', 'Nesta investigacao o aluno participar na construção de um foguetão', 70000, '2020-11-22', 'Bolsa para construir um foguetão', 16),
(11, '2021-02-02', 'Este trabalho ira ser levado a cabo ao longo de 1 ano e necesitamos de membros para o projeto jovem e dinâmico', 21000, '2020-12-15', 'Desenvolvimento de uma framework de gestão de recursos distribuidos', 12);

-- --------------------------------------------------------

--
-- Estrutura da tabela `grantdao_application_questions`
--

CREATE TABLE `grantdao_application_questions` (
  `grantdao_grant_id` bigint NOT NULL,
  `application_questions_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `grantdao_application_questions`
--

INSERT INTO `grantdao_application_questions` (`grantdao_grant_id`, `application_questions_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(3, 4),
(4, 5),
(4, 6),
(5, 7),
(5, 8),
(6, 9),
(6, 10),
(7, 11),
(7, 12),
(7, 13),
(8, 14),
(9, 15),
(10, 16),
(11, 17),
(11, 18);

-- --------------------------------------------------------

--
-- Estrutura da tabela `grant_questiondao`
--

CREATE TABLE `grant_questiondao` (
  `id` bigint NOT NULL,
  `field_description` varchar(255) DEFAULT NULL,
  `mandatory` bit(1) NOT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `grant_questiondao`
--

INSERT INTO `grant_questiondao` (`id`, `field_description`, `mandatory`, `type`) VALUES
(1, 'Porque acha que merece a bolsa?', b'1', 'string'),
(2, 'No seu ponto de vista, o que e ser carpinteiro?', b'1', 'string'),
(3, 'Quais são os seus conhecimentos na area em questão?', b'1', 'string'),
(4, 'Qual a sua area de preferencia?', b'0', 'string'),
(5, 'Quantas horas por dia passa a jogar?', b'1', 'string'),
(6, 'Qual a sua plataforma de jogos favorita?', b'0', 'string'),
(7, 'Qual é a sua experiencia em kotlin e Spring?', b'1', 'string'),
(8, 'Qual é a sua experiencia em react+typescritp?', b'1', 'string'),
(9, 'Usa mascara?', b'1', 'string'),
(10, 'Já teve Covid-19?', b'1', 'string'),
(11, 'Qual é a sua experiencia em JS e Typescript?', b'1', 'string'),
(12, 'Qual é a sua familiaridade com frameworks de JS?', b'1', 'string'),
(13, 'Qual é a sua motivação para pertencer a este projeto?', b'0', 'string'),
(14, 'Como se divide um atomo?', b'1', 'string'),
(15, 'No seu ponto de vista, o que e ser fuzileiro?', b'1', 'string'),
(16, 'Qual é a sua experiencia com foguetões?', b'1', 'string'),
(17, 'Tem ideia do que são recursos distribuidos?', b'1', 'string'),
(18, 'É um utilizador proeficiente em babel?', b'1', 'string');

-- --------------------------------------------------------

--
-- Estrutura da tabela `grant_quest_seq`
--

CREATE TABLE `grant_quest_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `grant_quest_seq`
--

INSERT INTO `grant_quest_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Estrutura da tabela `grant_responsedao`
--

CREATE TABLE `grant_responsedao` (
  `id` bigint NOT NULL,
  `response` varchar(255) DEFAULT NULL,
  `question_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `grant_responsedao`
--

INSERT INTO `grant_responsedao` (`id`, `response`, `question_id`) VALUES
(1, 'Porque sou muito bom!', 1),
(2, 'Porque sou muito trabalhador.', 1),
(3, 'Sei muito python', 3),
(4, 'Clustering', 4),
(5, 'Não sei nada sobre o tema.', 3),
(6, 'Clustering', 4),
(7, 'No minimo 5h por dia.', 5),
(8, 'Atari', 6),
(9, '3h por dia, não posso mais por causa da vista.', 5),
(10, 'XBox', 6),
(11, 'Muita, sou um master!', 7),
(12, 'Dou uns toques aqui e ali', 8),
(13, 'Sou uma máquina em spring', 7),
(14, 'É a minha cena!', 8),
(52, 'Saber fazer muitas flexões', 15),
(53, 'Saber respirar debaixo de agua', 15),
(54, 'Claro', 9),
(55, 'Não', 10),
(102, 'Uma arte', 2);

-- --------------------------------------------------------

--
-- Estrutura da tabela `grant_response_seq`
--

CREATE TABLE `grant_response_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `grant_response_seq`
--

INSERT INTO `grant_response_seq` (`next_val`) VALUES
(201);

-- --------------------------------------------------------

--
-- Estrutura da tabela `grant_seq`
--

CREATE TABLE `grant_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `grant_seq`
--

INSERT INTO `grant_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Estrutura da tabela `institutiondao`
--

CREATE TABLE `institutiondao` (
  `id` bigint NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_numb` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `institutiondao`
--

INSERT INTO `institutiondao` (`id`, `email`, `name`, `phone_numb`) VALUES
(1, 'email', 'TCF', '999'),
(2, 'helpdesk@fct.pt', 'FCT', '242452951'),
(3, 'helpdesk@ist.pt', 'IST', '265354814'),
(4, 'helpdesk@iscte.pt', 'ISCTE', '242359145'),
(5, 'helpdesk@ul.pt', 'UL', '251369428'),
(6, 'helpdesk@sbe.pt', 'SBE', '281623751');

-- --------------------------------------------------------

--
-- Estrutura da tabela `inst_seq`
--

CREATE TABLE `inst_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `inst_seq`
--

INSERT INTO `inst_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Estrutura da tabela `paneldao`
--

CREATE TABLE `paneldao` (
  `panelid` bigint NOT NULL,
  `grant_grant_id` bigint DEFAULT NULL,
  `panel_chair_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `paneldao`
--

INSERT INTO `paneldao` (`panelid`, `grant_grant_id`, `panel_chair_id`) VALUES
(1, 1, 18),
(2, 2, 21),
(3, 3, 19),
(4, 4, 18),
(5, 5, 17),
(6, 6, 17),
(7, 7, 20),
(8, 8, 18),
(9, 9, 18),
(10, 10, 18),
(11, 11, 21);

-- --------------------------------------------------------

--
-- Estrutura da tabela `paneldao_reviewers`
--

CREATE TABLE `paneldao_reviewers` (
  `paneldao_panelid` bigint NOT NULL,
  `reviewers_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `paneldao_reviewers`
--

INSERT INTO `paneldao_reviewers` (`paneldao_panelid`, `reviewers_id`) VALUES
(1, 18),
(1, 17),
(1, 22),
(1, 19),
(2, 21),
(2, 24),
(2, 18),
(2, 22),
(3, 19),
(3, 22),
(3, 25),
(3, 23),
(4, 18),
(4, 20),
(4, 21),
(4, 23),
(5, 17),
(5, 22),
(5, 24),
(5, 25),
(6, 17),
(6, 22),
(6, 24),
(6, 25),
(7, 20),
(7, 21),
(7, 23),
(7, 26),
(8, 18),
(8, 19),
(8, 23),
(8, 20),
(9, 18),
(9, 25),
(9, 26),
(9, 22),
(10, 18),
(10, 17),
(10, 25),
(10, 21),
(11, 21),
(11, 26),
(11, 22),
(11, 18);

-- --------------------------------------------------------

--
-- Estrutura da tabela `panel_seq`
--

CREATE TABLE `panel_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `panel_seq`
--

INSERT INTO `panel_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Estrutura da tabela `roledao`
--

CREATE TABLE `roledao` (
  `id` bigint NOT NULL,
  `role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `roledao`
--

INSERT INTO `roledao` (`id`, `role`) VALUES
(1, 'ADMIN'),
(2, 'STUDENT'),
(3, 'REVIEWER'),
(4, 'SPONSOR');

-- --------------------------------------------------------

--
-- Estrutura da tabela `role_seq`
--

CREATE TABLE `role_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `role_seq`
--

INSERT INTO `role_seq` (`next_val`) VALUES
(101);

-- --------------------------------------------------------

--
-- Estrutura da tabela `userdao`
--

CREATE TABLE `userdao` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `course` varchar(255) DEFAULT NULL,
  `institution_id` bigint DEFAULT NULL,
  `cv_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `userdao`
--

INSERT INTO `userdao` (`dtype`, `id`, `email`, `name`, `password`, `username`, `address`, `birth_date`, `phone_number`, `course`, `institution_id`, `cv_id`) VALUES
('UserDAO', 1, '', '', '$2a$10$Ol2Kqae8aziZhJOpHKZvPujN00Uq6HfA7Mz.Vaefl/e72H3kJeota', 'a', NULL, NULL, NULL, NULL, NULL, NULL),
('StudentDAO', 2, 'jpf.fernandes@fct.pt', 'Joao Fernandes', '$2a$10$JfrJPbIN1ZAPgdZyzBFrXeFi93gY8gtvAcgJP6qo/43FZEqUp/Rq6', 'jpf.fernandes', 'Rua das Meloas', '1998-04-29', NULL, 'MIEI', 2, 1),
('StudentDAO', 3, 'lf.rosario@fct.unl.pt', 'Luis Rosario', '$2a$10$CBC9kqm1YtTsYxaWA9UgLOVlzwDqAjoexo8i9Bn73pQoImtiWfsgO', 'lf.rosario', 'Rua das Morangos', '1998-10-05', NULL, 'MIEI', 2, 2),
('StudentDAO', 4, 'jm.monteiro@ist.com', 'João Monteiro', '$2a$10$xQ2GbeYmahuDlsJBJ7aFf.m57UePgUPt0H2HRQ.sbsOeXsJ5cFviq', 'lm.monteiro', 'Rua dos Abacaxis', '1997-02-17', NULL, 'LEEC', 3, 3),
('StudentDAO', 5, 'j.miguel@ist.pt', 'José Miguel', '$2a$10$Nh9jkbG0xyKN7n6ZCgKpl.TFBXKUaFkiLPv0AJGmnB5efHHVfinD2', 'j.miguel', 'Pego das nuvens', '1996-01-09', NULL, 'IT', 3, 4),
('StudentDAO', 6, 'a.costa@iscte.pt', 'Andre costa', '$2a$10$CSzQaafwor5aA4U7JaldguTCecEiX0qkp4e7TvtW.jmt7jEQaE/u.', 'a.costa', 'Rua das cestos', '1997-05-22', NULL, 'CTS', 4, 5),
('StudentDAO', 7, 'i.andre@iscte.pt', 'Inês André', '$2a$10$HUiQ/iZOlv1TB/CM7MbmcOCUBgszx8Gqn5dlZUEwPmvuPWKar/ZCa', 'i.andre', 'Travessa das altas novas', '1995-09-25', NULL, 'HIST', 4, 6),
('StudentDAO', 8, 'z.lopes@ul.pt', 'Zacarias Lopes', '$2a$10$Y/Yaf.umlrmUW.orT/AY/eJkU.PZ5bOhmPXVuEyd0ssWsOGZAtgoe', 'z.lopes', 'Travessa das altas novas', '1999-02-28', NULL, 'ELEC', 5, 7),
('StudentDAO', 9, 'a.nunes@ul.pt', 'Ana Nunes', '$2a$10$Au2qrTcpfptXyoDqj/Fdq.Fs5HCU7UU8exQiONsoTgFlWaWbICG7q', 'a.nunes', 'Travessa das altas novas', '1994-05-12', NULL, 'CONT', 5, 8),
('StudentDAO', 10, 'b.ricardo@sbe.pt', 'Bernardo Ricardo', '$2a$10$Nmd9fouvV9JiM0Ws.FNLCOfWbptS6WqC9piIRbVgpbL1tbHkt3VK6', 'b.ricardo', 'Travessa das altas novas', '1995-09-25', NULL, 'GEST', 6, 9),
('StudentDAO', 11, 'd.afonso@sbe.pt', 'Duarte Afonso', '$2a$10$FYZwCl23gR4uh0O/.fP1feHxnzJKKeBxxYyiN4dxDILUCLZcexpMS', 'd.afonso', 'Travessa das altas novas', '2000-07-30', NULL, 'MKT', 6, 10),
('SponsorDAO', 12, 'premios_nobel@email.pt', 'Premios Nobel', '$2a$10$GeFczLtg8NaiOYy9M84eNOY2U.89T8KILX5TxxUFwOVy7uOrzUTZW', 'nobel', NULL, NULL, '263486129', NULL, NULL, NULL),
('SponsorDAO', 13, 'patrocinador@email.pt', 'Patricio Patrocinador', '$2a$10$7N4QmM79qMb9z1hx3yYetOqGLqj2Y3ibQsBVd1xfiOQNxldQtscJ.', 'patrocinador', NULL, NULL, '284159753', NULL, NULL, NULL),
('SponsorDAO', 14, 'governo@email.pt', 'Governo Portugues', '$2a$10$q243sBoM7hLz5F82/cSxDe92WPU2aUmNpYOoAvNiVLGynM9l12Fem', 'governo', NULL, NULL, '212658258', NULL, NULL, NULL),
('SponsorDAO', 15, 'cern@email.pt', 'CERN', '$2a$10$wEwQUqL6sLtYO5.fj1sYiOd0BJeHxAuTf28o6fbcLQnfIyU9I983a', 'cern', NULL, NULL, '259214685', NULL, NULL, NULL),
('SponsorDAO', 16, 'nasa@email.pt', 'NASA', '$2a$10$mblGFf5azXiVpSWTeZ9qwOILTRYk6cpyppevib58agfs8VWIdYYoO', 'nasa', NULL, NULL, '235215486', NULL, NULL, NULL),
('ReviewerDAO', 17, 'seco@fct.pt', 'Joao Seco', '$2a$10$rffvHjTgliWU1W4PJJOqFeDW2MjydoFu4BsUzMDDmiRUlluby0NAi', 'seco.joao', 'Rua das Peras', '1980-05-22', NULL, NULL, 2, NULL),
('ReviewerDAO', 18, 'geraldo@fct.pt', 'Eduardo Geraldo', '$2a$10$H5zLEtSJMtai8mcnW..ALOfB/qKRymtp3pSqplyhKtnBOcWeA2qAO', 'geraldo.edu', 'Rua das Macieiras', '1995-11-30', NULL, NULL, 2, NULL),
('ReviewerDAO', 19, 'leitao@ist.pt', 'Joao Leitao', '$2a$10$LWvjF9ehX3zOA38yuXyTHOpb3JYcY2sTnLcu3zdivF12tZEcMjjuS', 'leitao.joao', 'Rua das Pereiras', '1985-06-08', NULL, NULL, 3, NULL),
('ReviewerDAO', 20, 'gou@ist.pt', 'Miguel Goulao', '$2a$10$W2Kp7u.12DKfVwn1GfM95.JFt5XkYNUT9hjcMr0QPKfq5/wVkexzq', 'goulao.miguel', 'Rua das Nespras', '1975-04-16', NULL, NULL, 3, NULL),
('ReviewerDAO', 21, 'per@iscte.pt', 'Nuno Preguiça', '$2a$10$.nRIvjfi2Ilp5BXMbNpPw.pKdGy9ksAM3H4dF9zBE8jEhJnKd8Rga', 'preguica.nuno', 'Rua das Oliveiras', '1970-06-23', NULL, NULL, 4, NULL),
('ReviewerDAO', 22, 'sof@iscte.pt', 'Sofia Cavaco', '$2a$10$8jzrkqmGsgxyUvDOBRqMmuhG2jr8.WSvHGzy1lIyDnSLakz8QSFrm', 'cavaco.sofia', 'Rua das Vinhas', '1987-12-20', NULL, NULL, 4, NULL),
('ReviewerDAO', 23, 'leg@ul.pt', 'Jose Legatheaux', '$2a$10$nnEghf.cn2YO9LmFYxSiXeeCUnOPqqllSIwSY2agi.gGXsviMeY8S', 'legatheaux.jose', 'Rua das Cerejas', '1950-03-15', NULL, NULL, 5, NULL),
('ReviewerDAO', 24, 'cai@ul.pt', 'Luis Caires', '$2a$10$UxNZS6KjMaX8ObAlYBW7Ne.k/b6HgZ.UCLUgLzyAGtGTVjeYjfPJW', 'caires.luis', 'Rua das Aboboras', '1973-09-26', NULL, NULL, 5, NULL),
('ReviewerDAO', 25, 'lour@sbe.pt', 'João Lourenço', '$2a$10$73PX7aE3rsGqopSxARfbQuojVSFLjrdCRugh1NKGID3drYEbB2.rq', 'lourenco.joao', 'Rua das Castanhas', '1975-04-15', NULL, NULL, 6, NULL),
('ReviewerDAO', 26, 'rav@sbe.pt', 'Antonio Ravara', '$2a$10$fyB3cj0JJzTJKdTh4D9YEOJcCzj9kyLJaY3RJruKfn5XJRGb9kBXK', 'ravara.antonio', 'Rua das Bolotas', '1977-06-29', NULL, NULL, 6, NULL);

-- --------------------------------------------------------

--
-- Estrutura da tabela `userdao_roles`
--

CREATE TABLE `userdao_roles` (
  `userdao_id` bigint NOT NULL,
  `roles_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `userdao_roles`
--

INSERT INTO `userdao_roles` (`userdao_id`, `roles_id`) VALUES
(2, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(9, 2),
(10, 2),
(11, 2),
(12, 4),
(13, 4),
(14, 4),
(15, 4),
(16, 4),
(17, 3),
(18, 3),
(19, 3),
(20, 3),
(21, 3),
(22, 3),
(23, 3),
(24, 3),
(25, 3),
(26, 3),
(1, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `user_seq`
--

CREATE TABLE `user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Extraindo dados da tabela `user_seq`
--

INSERT INTO `user_seq` (`next_val`) VALUES
(101);

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `applicationdao`
--
ALTER TABLE `applicationdao`
  ADD PRIMARY KEY (`applicationid`),
  ADD KEY `FKsg7bfs2qlx2ngbqp468rjdgk9` (`grant_grant_id`),
  ADD KEY `FKrtsum5dx7aeeabd6vky4kobht` (`student_id`);

--
-- Índices para tabela `applicationdao_responses`
--
ALTER TABLE `applicationdao_responses`
  ADD UNIQUE KEY `UK_gi88b66sye7yd2c4lgw5n3i3f` (`responses_id`),
  ADD KEY `FKfpc6nh6qnkigu2neclpej018u` (`applicationdao_applicationid`);

--
-- Índices para tabela `cvdao`
--
ALTER TABLE `cvdao`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `cvdao_fields`
--
ALTER TABLE `cvdao_fields`
  ADD UNIQUE KEY `UK_20nhox2luonf6c5ey41qckotf` (`fields_id`),
  ADD KEY `FKa39u345afh8yc0qcxet66le7e` (`cvdao_id`);

--
-- Índices para tabela `evaluationdao`
--
ALTER TABLE `evaluationdao`
  ADD PRIMARY KEY (`eval_id`),
  ADD KEY `FKqut2p8ixy50sjmwqm2w0kfe8x` (`app_applicationid`),
  ADD KEY `FK56aov8uwxx15d3aphg1mu8gm2` (`grant_grant_id`),
  ADD KEY `FKhi4lihqx9erko7bh1snd7j6hc` (`rev_id`);

--
-- Índices para tabela `fielddao`
--
ALTER TABLE `fielddao`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `grantdao`
--
ALTER TABLE `grantdao`
  ADD PRIMARY KEY (`grant_id`),
  ADD KEY `FKjp4xv7o5qx0bphthxjmuwjmjs` (`sponsor_id`);

--
-- Índices para tabela `grantdao_application_questions`
--
ALTER TABLE `grantdao_application_questions`
  ADD UNIQUE KEY `UK_j8w9jv5197apsbogufpndia39` (`application_questions_id`),
  ADD KEY `FK4oif6s41kmbmfchbq3qj5j4kq` (`grantdao_grant_id`);

--
-- Índices para tabela `grant_questiondao`
--
ALTER TABLE `grant_questiondao`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `grant_responsedao`
--
ALTER TABLE `grant_responsedao`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3xgxtwafwub4eb2ym4jx1t2u1` (`question_id`);

--
-- Índices para tabela `institutiondao`
--
ALTER TABLE `institutiondao`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `paneldao`
--
ALTER TABLE `paneldao`
  ADD PRIMARY KEY (`panelid`),
  ADD KEY `FKkg4tht4qtle43nat44hoykrm8` (`grant_grant_id`),
  ADD KEY `FKlohjfy1xj92lu8dd8o8aolovb` (`panel_chair_id`);

--
-- Índices para tabela `paneldao_reviewers`
--
ALTER TABLE `paneldao_reviewers`
  ADD KEY `FKt2oc8knbdm30wbnra8ovknv4v` (`reviewers_id`),
  ADD KEY `FKqgn1ulq1xbhe9wdyx2osg95fm` (`paneldao_panelid`);

--
-- Índices para tabela `roledao`
--
ALTER TABLE `roledao`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `userdao`
--
ALTER TABLE `userdao`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4xwf0n02p1cf8iqescw99t6fj` (`institution_id`),
  ADD KEY `FKalnhfhjx5xc8n1swnvliom4fv` (`cv_id`);

--
-- Índices para tabela `userdao_roles`
--
ALTER TABLE `userdao_roles`
  ADD KEY `FKjcnmsa79l4jxt5qavwoyp6mhn` (`roles_id`),
  ADD KEY `FKnv90c9t8hxjku1k1qbdk1svld` (`userdao_id`);

--
-- Restrições para despejos de tabelas
--

--
-- Limitadores para a tabela `applicationdao`
--
ALTER TABLE `applicationdao`
  ADD CONSTRAINT `FKrtsum5dx7aeeabd6vky4kobht` FOREIGN KEY (`student_id`) REFERENCES `userdao` (`id`),
  ADD CONSTRAINT `FKsg7bfs2qlx2ngbqp468rjdgk9` FOREIGN KEY (`grant_grant_id`) REFERENCES `grantdao` (`grant_id`);

--
-- Limitadores para a tabela `applicationdao_responses`
--
ALTER TABLE `applicationdao_responses`
  ADD CONSTRAINT `FKfpc6nh6qnkigu2neclpej018u` FOREIGN KEY (`applicationdao_applicationid`) REFERENCES `applicationdao` (`applicationid`),
  ADD CONSTRAINT `FKkrjyshvb1f0gta3s9ixv0566n` FOREIGN KEY (`responses_id`) REFERENCES `grant_responsedao` (`id`);

--
-- Limitadores para a tabela `cvdao_fields`
--
ALTER TABLE `cvdao_fields`
  ADD CONSTRAINT `FK68bafc8dgr9mp27ugy9eh42ss` FOREIGN KEY (`fields_id`) REFERENCES `fielddao` (`id`),
  ADD CONSTRAINT `FKa39u345afh8yc0qcxet66le7e` FOREIGN KEY (`cvdao_id`) REFERENCES `cvdao` (`id`);

--
-- Limitadores para a tabela `evaluationdao`
--
ALTER TABLE `evaluationdao`
  ADD CONSTRAINT `FK56aov8uwxx15d3aphg1mu8gm2` FOREIGN KEY (`grant_grant_id`) REFERENCES `grantdao` (`grant_id`),
  ADD CONSTRAINT `FKhi4lihqx9erko7bh1snd7j6hc` FOREIGN KEY (`rev_id`) REFERENCES `userdao` (`id`),
  ADD CONSTRAINT `FKqut2p8ixy50sjmwqm2w0kfe8x` FOREIGN KEY (`app_applicationid`) REFERENCES `applicationdao` (`applicationid`);

--
-- Limitadores para a tabela `grantdao`
--
ALTER TABLE `grantdao`
  ADD CONSTRAINT `FKjp4xv7o5qx0bphthxjmuwjmjs` FOREIGN KEY (`sponsor_id`) REFERENCES `userdao` (`id`);

--
-- Limitadores para a tabela `grantdao_application_questions`
--
ALTER TABLE `grantdao_application_questions`
  ADD CONSTRAINT `FK4oif6s41kmbmfchbq3qj5j4kq` FOREIGN KEY (`grantdao_grant_id`) REFERENCES `grantdao` (`grant_id`),
  ADD CONSTRAINT `FKrvaaxd4ni7mfc18uf6ddxxjxc` FOREIGN KEY (`application_questions_id`) REFERENCES `grant_questiondao` (`id`);

--
-- Limitadores para a tabela `grant_responsedao`
--
ALTER TABLE `grant_responsedao`
  ADD CONSTRAINT `FK3xgxtwafwub4eb2ym4jx1t2u1` FOREIGN KEY (`question_id`) REFERENCES `grant_questiondao` (`id`);

--
-- Limitadores para a tabela `paneldao`
--
ALTER TABLE `paneldao`
  ADD CONSTRAINT `FKkg4tht4qtle43nat44hoykrm8` FOREIGN KEY (`grant_grant_id`) REFERENCES `grantdao` (`grant_id`),
  ADD CONSTRAINT `FKlohjfy1xj92lu8dd8o8aolovb` FOREIGN KEY (`panel_chair_id`) REFERENCES `userdao` (`id`);

--
-- Limitadores para a tabela `paneldao_reviewers`
--
ALTER TABLE `paneldao_reviewers`
  ADD CONSTRAINT `FKqgn1ulq1xbhe9wdyx2osg95fm` FOREIGN KEY (`paneldao_panelid`) REFERENCES `paneldao` (`panelid`),
  ADD CONSTRAINT `FKt2oc8knbdm30wbnra8ovknv4v` FOREIGN KEY (`reviewers_id`) REFERENCES `userdao` (`id`);

--
-- Limitadores para a tabela `userdao`
--
ALTER TABLE `userdao`
  ADD CONSTRAINT `FK4xwf0n02p1cf8iqescw99t6fj` FOREIGN KEY (`institution_id`) REFERENCES `institutiondao` (`id`),
  ADD CONSTRAINT `FKalnhfhjx5xc8n1swnvliom4fv` FOREIGN KEY (`cv_id`) REFERENCES `cvdao` (`id`);

--
-- Limitadores para a tabela `userdao_roles`
--
ALTER TABLE `userdao_roles`
  ADD CONSTRAINT `FKjcnmsa79l4jxt5qavwoyp6mhn` FOREIGN KEY (`roles_id`) REFERENCES `roledao` (`id`),
  ADD CONSTRAINT `FKnv90c9t8hxjku1k1qbdk1svld` FOREIGN KEY (`userdao_id`) REFERENCES `userdao` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
