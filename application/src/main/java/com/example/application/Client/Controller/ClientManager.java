package com.example.application.Client.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import com.example.application.ApplicationServer.Controller.CreditManager;
import com.example.application.ApplicationServer.Controller.DiceController;
import com.example.application.ClientManagementServer.DatabaseAccess; //
import com.example.application.ClientManagementServer.RankRecord; //

import java.util.UUID;

@Controller
public class ClientManager {
    private final DatabaseAccess dbAccess = new DatabaseAccess();
    
    private DiceController diceController;
    private CreditManager creditManager;
    private int currentPosition = 0;

    public ClientManager(DiceController diceController, CreditManager creditManager) {
        this.diceController = diceController;
        this.creditManager = creditManager;
    }

    @GetMapping("/")
    public String home() {
        System.out.println("[Access] Home Screen (login.html)");
        return "home";
    }

    @GetMapping("/start")
    public String start(HttpSession session) {
        String user = (String) session.getAttribute("loginName");
        System.out.println("[Access] Start Screen - User: " + user);
        return "start";
    }

    @GetMapping("/game")
    public String index(Model model, HttpSession session) {
        String user = (String) session.getAttribute("loginName");
        System.out.println("[Game] Initializing game for: " + user);
        
        // データの初期化
        this.currentPosition = 0; 
        creditManager.reset();

        model.addAttribute("earnedUnits", 0);
        model.addAttribute("expectedUnits", 25);
        model.addAttribute("result", "ダイスを振ってください");
        
        System.out.println("[Game] State Reset: Position=0, Credits=0");
        return "game";
    }

    /**
     * 戦績表示: DatabaseAccess からデータを取得し、ログに出力
     */
    @GetMapping("/score")
    public String showScore(HttpSession session, Model model) {
        String loginName = (String) session.getAttribute("loginName");
        System.out.println("[Score] Requesting records for: " + loginName);

        if (loginName != null) {
            // DBからランク情報を取得
            RankRecord record = dbAccess.getRankRecordByUsername(loginName);
            
            // 詳細なログ出力
            System.out.println("--- DB Fetch Results ---");
            System.out.println("  1st Place: " + record.rank1());
            System.out.println("  2nd Place: " + record.rank2());
            System.out.println("  3rd Place: " + record.rank3());
            System.out.println("  4th Place: " + record.rank4());
            System.out.println("------------------------");

            model.addAttribute("username", loginName);
            model.addAttribute("record", record);
        } else {
            System.out.println("[Score] Warning: No user in session.");
        }
        return "score"; 
    }

    /**
     * ログイン処理の詳細ログ
     */
    @PostMapping("/login-process")
    public String processLogin(@RequestParam("username") String name,
                               @RequestParam("password") String pass,
                               Model model,
                               HttpSession session) {
        
        System.out.println("[Login] Attempting login for: " + name);
        
        // DatabaseAccess で照合
        var user = dbAccess.getUserByUsername(name);

        if (user != null) {
            System.out.println("[Login] User found. Verifying password...");
            if (user.password().equals(pass)) {
                System.out.println("[Login] SUCCESS: " + name);
                session.setAttribute("loginName", name);
                return "redirect:/start";
            } else {
                System.out.println("[Login] FAILED: Invalid password for " + name);
            }
        } else {
            System.out.println("[Login] FAILED: User " + name + " not found in DB.");
        }

        model.addAttribute("error", "ユーザ名またはパスワードが間違っています!!");
        return "home"; 
    }

    /**
     * アカウント登録処理の詳細ログ
     */
    @PostMapping("/register-process")
    public String processSignup(@RequestParam("username") String name,
                                @RequestParam("password") String pass,
                                Model model) {
        
        System.out.println("[Register] Attempting signup for: " + name);
        
        // 重複チェック
        if (dbAccess.getUserByUsername(name) != null) {
            System.out.println("[Register] FAILED: Username '" + name + "' is already taken.");
            model.addAttribute("error", "既に登録されています。");
            return "home";
        }
        
        // 登録実行
        String id = UUID.randomUUID().toString();
        System.out.println("[Register] Creating new account...");
        System.out.println("  Username: " + name);
        System.out.println("  ID: " + id);
        
        dbAccess.createUser(name, pass, id); 
        System.out.println("[Register] SUCCESS: " + name + " created.");

        return "redirect:/"; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String user = (String) session.getAttribute("loginName");
        System.out.println("[Logout] User: " + user);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/rule")
    public String rule() {
        return "rule";
    }

    @GetMapping("/matchingWait")
    public String matchingWait() {
        return "/matchingWait";
    }
    
}
