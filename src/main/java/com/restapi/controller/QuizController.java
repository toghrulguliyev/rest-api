package com.restapi.controller;

import com.restapi.model.Response;
import com.restapi.model.User;
import com.restapi.model.quizDuel.Duel;
import com.restapi.model.quizDuel.Question;
import com.restapi.model.quizDuel.Results;
import com.restapi.model.quizDuel.Score;
import com.restapi.repository.DuelRepository;
import com.restapi.repository.QuestionRepository;
import com.restapi.repository.ScoreRepository;
import com.restapi.repository.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class QuizController {

    @Autowired
    DuelRepository duelRepository;
    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebController webController;

    @PostMapping("save_question")
    public ResponseEntity<Response> saveQuestion(@RequestBody Document doc) {
        Question question1 = new Question((String) doc.get("question"), (List<String>) doc.get("answers"), (String) doc.get("correctAnswer"), (String) doc.get("category"));
        Question newQ = questionRepository.save(question1);
        Response response = new Response(newQ.getId().toString(), 201);
        return new ResponseEntity<Response>(response, HttpStatus.CREATED);

    }

    @PostMapping("get_duels")
    public ResponseEntity<List<Duel>> getDuels(@RequestParam("username") String username) {
        List<Duel> autorDuels = new ArrayList<Duel>();
        List<Duel> oppoDuels = new ArrayList<Duel>();
        List<Duel> duels = new ArrayList<Duel>();
        autorDuels = duelRepository.findAllByAutor(username);
        oppoDuels = duelRepository.findAllByOpponent(username);
        if ((autorDuels == null || autorDuels.isEmpty()) && (oppoDuels == null || oppoDuels.isEmpty())) {
            return new ResponseEntity<List<Duel>>(HttpStatus.NO_CONTENT);
        } else if ((autorDuels != null && !autorDuels.isEmpty()) && (oppoDuels == null || oppoDuels.isEmpty())) {
            return new ResponseEntity<List<Duel>>(autorDuels, HttpStatus.OK);
        } else if ((autorDuels == null || autorDuels.isEmpty()) && (oppoDuels != null && !oppoDuels.isEmpty())) {
            return new ResponseEntity<List<Duel>>(oppoDuels, HttpStatus.OK);
        } else if ((autorDuels != null && !autorDuels.isEmpty()) && (oppoDuels != null && !oppoDuels.isEmpty())) {
            for (Duel duel : autorDuels) {
                duels.add(duel);
            }
            for (Duel duel : oppoDuels) {
                duels.add(duel);
            }
//            for (Duel duel1 : autorDuels) {
//                for (Duel duel2 : oppoDuels) {
//                   if (duel1.getAutor().equals(duel2.getOpponent())) {
//                       duels.add(duel2);
//                   }
//                }
//            }
            return new ResponseEntity<List<Duel>>(duels, HttpStatus.OK);
        }
        return new ResponseEntity<List<Duel>>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("get_results")
    public ResponseEntity<Results> getOverallScore(@RequestParam("username") String username) {
        List<Duel> autorDuels = new ArrayList<Duel>();
        List<Duel> oppoDuels = new ArrayList<Duel>();
        List<Duel> duels = new ArrayList<Duel>();
        List<Score> scores = new ArrayList<Score>();
        autorDuels = duelRepository.findAllByAutor(username);
        oppoDuels = duelRepository.findAllByOpponent(username);
        if ((autorDuels == null || autorDuels.isEmpty()) && (oppoDuels == null || oppoDuels.isEmpty())) {
            return new ResponseEntity<Results>(HttpStatus.NO_CONTENT);
        } else if ((autorDuels != null && !autorDuels.isEmpty()) && (oppoDuels == null || oppoDuels.isEmpty())) {
            for (Duel duel : autorDuels) {
                if (duel.isAutorStatus()) {
                    scores.add(duel.getScore());
                }
            }
        } else if ((autorDuels == null || autorDuels.isEmpty()) && (oppoDuels != null && !oppoDuels.isEmpty())) {
            for (Duel duel : oppoDuels) {
                if (duel.isOpponentStatus()) {
                    scores.add(duel.getScore());
                }
            }
        } else if ((autorDuels != null && !autorDuels.isEmpty()) && (oppoDuels != null && !oppoDuels.isEmpty())) {
            for (Duel duel : autorDuels) {
                duels.add(duel);
            }
            for (Duel duel1 : autorDuels) {
                for (Duel duel2 : oppoDuels) {
                    if (!duel1.getId().equals(duel2.getId())) {
                        duels.add(duel2);
                    }
                }
            }

            for (Duel duel : duels) {
                if (duel.getAutor().equals(username)) {
                    if (duel.isAutorStatus()) {
                        scores.add(duel.getScore());
                    }
                } else if (duel.getOpponent().equals(username)) {
                    if (duel.isOpponentStatus()) {
                        scores.add(duel.getScore());
                    }
                }
            }
        }

            if (scores == null || scores.isEmpty()) {
                return new ResponseEntity<Results>(HttpStatus.NO_CONTENT);
            } else if (scores != null && !scores.isEmpty()) {
                Results results = new Results();
                results.setPlayedDuels(scores.size());
                results.setAnsweredQuestions(scores.size() * 10);
                int right = 0;
                for (Score score : scores) {
                    if (username.equals(score.getAutor())) {
                        right = right + score.getScoreAutor();
                    } else if (username.equals(score.getOpponent())) {
                        right = right + score.getScoreOpponent();
                    }
                }
                results.setRightAnswered(right);
                int wrong = 0;
                for (Score score : scores) {
                    if (username.equals(score.getAutor())) {
                        wrong = wrong + (10 - score.getScoreAutor());
                    } else if (username.equals(score.getOpponent())) {
                        wrong = wrong + (10 - score.getScoreOpponent());
                    }
                }
                results.setWrongAnswered(wrong);
                double wrong_ans = (wrong * 10) / scores.size();
                double right_ans = (right * 10) / scores.size();
                results.setAnsweredWrong(wrong_ans);
                results.setAnsweredRight(right_ans);
                return new ResponseEntity<Results>(results, HttpStatus.OK);
            }
            return new ResponseEntity<Results>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("create_duel")
    public ResponseEntity<Response> createDuel(@RequestParam("username") String username, @RequestParam("category") String category, @RequestParam("opponent") String opponent) {

        List<Question> questions = new ArrayList<Question>();
        List<Question> randomQuestions = new ArrayList<Question>();
        //questions = questionRepository.findAllByCategory(category);
        questions = questionRepository.getByCategory(category);
        if (questions == null || questions.isEmpty()) {
            Response response = new Response("Keine Fragen vorhanden", 400);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        if (questions != null && !questions.isEmpty()) {
            while (randomQuestions.size() < 10) {
                int random = new Random().nextInt(questions.size());
                if (randomQuestions == null || randomQuestions.isEmpty()) {
                    randomQuestions.add(questions.get(random));
                } else if (randomQuestions != null && !randomQuestions.isEmpty()) {
                    if (!randomQuestions.contains(questions.get(random))) {
                        randomQuestions.add(questions.get(random));
                    }
                }
            }
            Duel duel = new Duel(username, opponent, randomQuestions, "Aktiv", category);
            duelRepository.save(duel);
            scoreRepository.save(duel.getScore());
            User user = userRepository.findByUsername(opponent);
            if (user != null) {
                if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                    webController.newFamilyEventNotification(username + " hat ein neues Duell erstellt", "Fragenkaterogie: " + duel.getCategory(), user.getFcmToken());
                }
            }
            Response response = new Response("Duell erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        }
        Response response = new Response("Duell nicht erzeugt", 400);
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("save_score")
    public ResponseEntity<Response> saveScore(@RequestParam("username") String username, @RequestParam("type") String type, @RequestParam("id") String id, @RequestParam("score") int score) {

        Duel duel = duelRepository.getById(id);

        if (duel == null) {
            Response response = new Response("Duell nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            if (type.equals("autor")) {
                duel.setAutorScore(score);
                duel.setAutorStatus(true);
            } else if (type.equals("opponent")) {
                duel.setOpponentScore(score);
                duel.setOpponentStatus(true);
            }
            if (duel.isOpponentStatus() && duel.isAutorStatus()) {
                if (duel.getAutorScore() > duel.getOpponentScore()) {
                    duel.setWinner(duel.getAutor());
                } else if (duel.getAutorScore() < duel.getOpponentScore()) {
                    duel.setWinner(duel.getOpponent());
                } else if (duel.getAutorScore() == duel.getOpponentScore()) {
                    duel.setWinner("Unentschieden");
                }
                duel.setStatus("Spiel beendet");
            } else if (!duel.isOpponentStatus() && duel.isAutorStatus()) {
                duel.setStatus("Erwarte " + duel.getOpponent());
            } else if (duel.isOpponentStatus() && !duel.isAutorStatus()) {
                duel.setStatus("Erwarte " + duel.getAutor());
            }
            duelRepository.save(duel);

            if (type.equals("autor")) {
                User user = userRepository.findByUsername(duel.getAutor());
                if (user != null) {
                    if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                        webController.newFamilyEventNotification(username + " hat gerade gespielt", "Fragenkaterogie: " + duel.getCategory(), user.getFcmToken());
                    }
                }
            } else if (type.equals("opponent")) {
                User user = userRepository.findByUsername(duel.getOpponent());
                if (user != null) {
                    if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                        webController.newFamilyEventNotification(username + " hat gerade gespielt", "Fragenkaterogie: " + duel.getCategory(), user.getFcmToken());
                    }
                }
            }
            Response response = new Response("Punktestand aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("get_my_results")
    public ResponseEntity<Results> getResults(@RequestParam("username") String username) {

        Results results = new Results();
        List<Score> scores = new ArrayList<Score>();
        List<Score> autorScore = new ArrayList<Score>();
        List<Score> oppoScore = new ArrayList<Score>();
        autorScore = scoreRepository.findAllByAutor(username);
        oppoScore = scoreRepository.findAllByOpponent(username);

        //autor_duels = duelRepository.findAllByAutor(username);


        if ((autorScore == null || autorScore.isEmpty()) && (oppoScore == null || oppoScore.isEmpty())) {
            return new ResponseEntity<Results>(HttpStatus.NO_CONTENT);
        } else if ((autorScore != null && !autorScore.isEmpty()) && (oppoScore == null || oppoScore.isEmpty())) {
            scores = autorScore;

        } else if ((autorScore == null || autorScore.isEmpty()) && (oppoScore != null && !oppoScore.isEmpty())) {
            scores = oppoScore;
        } else if ((autorScore != null && !oppoScore.isEmpty()) && (oppoScore != null && !oppoScore.isEmpty())) {
            for (Score score : autorScore) {
                scores.add(score);
            }
            for (Score score1 : autorScore) {
                for (Score score2 : oppoScore) {
                    if (!score1.getId().equals(score2.getId())) {
                        scores.add(score2);
                    }
                }
            }
        }
        results.setPlayedDuels(scores.size());
        results.setAnsweredQuestions(scores.size() * 10);
        int right = 0;
        for (Score score : scores) {
            if (username.equals(score.getAutor())) {
                right = right + score.getScoreAutor();
            } else if (username.equals(score.getOpponent())) {
                right = right + score.getScoreOpponent();
            }
        }
        results.setRightAnswered(right);
        int wrong = 0;
        for (Score score : scores) {
            if (username.equals(score.getAutor())) {
                wrong = wrong + (10 - score.getScoreAutor());
            } else if (username.equals(score.getOpponent())) {
                wrong = wrong + (10 - score.getScoreOpponent());
            }
        }
        results.setWrongAnswered(wrong);
        results.setAnsweredWrong((wrong/(scores.size() * 10)) * 100);
        results.setAnsweredRight((right/(scores.size() * 10)) * 100);
        return new ResponseEntity<Results>(results, HttpStatus.OK);
    }

    @PostMapping("remove_duel")
    public ResponseEntity<Response> removeDuel(@RequestParam("id") String id) {

        long check = duelRepository.removeDuelById(id);

        if (check == 1) {
            Response response = new Response("Duell erfolgreich gelöscht", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            Response response = new Response("Duell löschen fehlgeschlagen", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }

    }

}