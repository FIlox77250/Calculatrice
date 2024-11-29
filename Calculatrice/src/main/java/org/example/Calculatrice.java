import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Calculatrice extends JFrame {
    private JTextField ecran;
    private JPanel mainPanel;
    private JPanel boutonsPanel;
    private double nombre1;
    private String operation = "";
    private boolean debut = true;
    private boolean modeScientifique = false;
    private DecimalFormat df = new DecimalFormat("#.##########");
    private double memoryValue = 0;

    private static final int LARGEUR_BASE = 300;
    private static final int LARGEUR_SCIENTIFIQUE = 500;
private static final int HAUTEUR_BASE = 500;

    private static final Color COULEUR_FOND = new Color(32, 32, 32);
    private static final Color COULEUR_BOUTONS = new Color(45, 45, 45);
    private static final Color COULEUR_OPERATEURS = new Color(209, 167, 255);
    private static final Color COULEUR_SCIENTIFIQUE = new Color(50, 50, 50);
    private static final Color COULEUR_TEXTE = Color.WHITE;
    private static final Color COULEUR_ECRAN = new Color(25, 25, 25);
    private static final Color COULEUR_SURVOL_OPERATEURS = new Color(219, 187, 255);
    private static final Color COULEUR_SURVOL_BOUTONS = new Color(60, 60, 60);

    public Calculatrice() {
        super("Brclacular");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialiserUI();
        ajouterSupportPaveNumerique();
    }

    private void initialiserUI() {
        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBackground(COULEUR_FOND);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COULEUR_FOND);
        JMenu viewMenu = new JMenu("Affichage");
        viewMenu.setForeground(COULEUR_TEXTE);
        JMenuItem toggleView = new JMenuItem(modeScientifique ? "Standard" : "Scientifique");
        toggleView.addActionListener(e -> basculerMode());
        viewMenu.add(toggleView);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        ecran = new JTextField("0");
        ecran.setEditable(false);
        ecran.setHorizontalAlignment(JTextField.RIGHT);
        ecran.setFont(new Font("Segoe UI", Font.BOLD, 32));
        ecran.setBackground(COULEUR_ECRAN);
        ecran.setForeground(COULEUR_TEXTE);
        ecran.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COULEUR_FOND, 5),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.add(ecran, BorderLayout.NORTH);

        boutonsPanel = new JPanel(new GridLayout(6, 4, 2, 2));
        boutonsPanel.setBackground(COULEUR_FOND);
        boutonsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        ajouterBoutonsStandard();

        mainPanel.add(boutonsPanel, BorderLayout.CENTER);
        add(mainPanel);

        setSize(LARGEUR_BASE, HAUTEUR_BASE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void ajouterSupportPaveNumerique() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_NUMPAD0 -> traiterTouche("0");
                    case KeyEvent.VK_NUMPAD1 -> traiterTouche("1");
                    case KeyEvent.VK_NUMPAD2 -> traiterTouche("2");
                    case KeyEvent.VK_NUMPAD3 -> traiterTouche("3");
                    case KeyEvent.VK_NUMPAD4 -> traiterTouche("4");
                    case KeyEvent.VK_NUMPAD5 -> traiterTouche("5");
                    case KeyEvent.VK_NUMPAD6 -> traiterTouche("6");
                    case KeyEvent.VK_NUMPAD7 -> traiterTouche("7");
                    case KeyEvent.VK_NUMPAD8 -> traiterTouche("8");
                    case KeyEvent.VK_NUMPAD9 -> traiterTouche("9");
                    case KeyEvent.VK_DECIMAL -> traiterTouche(".");
                    
                    case KeyEvent.VK_ADD -> traiterTouche("+");
                    case KeyEvent.VK_SUBTRACT -> traiterTouche("-");
                    case KeyEvent.VK_MULTIPLY -> traiterTouche("×");
                    case KeyEvent.VK_DIVIDE -> traiterTouche("÷");
                    case KeyEvent.VK_ENTER -> traiterTouche("=");
                    
                    case KeyEvent.VK_BACK_SPACE -> traiterTouche("⌫");
                    case KeyEvent.VK_ESCAPE -> traiterTouche("C");
                    case KeyEvent.VK_DELETE -> traiterTouche("CE");
                }
                
                char key = e.getKeyChar();
                if (Character.isDigit(key)) {
                    traiterTouche(String.valueOf(key));
                }
            }
        });
        
        setFocusable(true);
        requestFocus();
        
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                requestFocus();
            }
        });
    }

    private void ajouterBoutonsStandard() {
        boutonsPanel.removeAll();
        boutonsPanel.setLayout(new GridLayout(6, 4, 2, 2));

        String[] boutonsStandard = {
                "%", "CE", "C", "⌫",
                "1/x", "x²", "√", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "±", "0", ".", "="
        };

        for (String texte : boutonsStandard) {
            JButton bouton = creerBouton(texte);
            boutonsPanel.add(bouton);
        }
    }

    private void ajouterBoutonsScientifique() {
        boutonsPanel.removeAll();
        boutonsPanel.setLayout(new GridLayout(7, 5, 2, 2)); 
    
        String[] boutonsScientifique = {
                "MC", "MR", "M+", "M-", "MS",     
                "%", "CE", "C", "⌫", "÷",      
                "x²", "√", "7", "8", "9",         
                "sin", "cos", "4", "5", "6",     
                "tan", "log", "1", "2", "3",    
                "ln", "exp", "0", ".", "±",       
                "1/x", "(", ")", "×", "="       
        };
    
        for (String texte : boutonsScientifique) {
            JButton bouton = creerBouton(texte);
            boutonsPanel.add(bouton);
        }
    }

    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bouton.setForeground(COULEUR_TEXTE);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setOpaque(true);

        if (texte.matches("[0-9.]")) {
            bouton.setBackground(COULEUR_BOUTONS);
        } else if (texte.matches("[÷×\\-+=]")) {
            bouton.setBackground(COULEUR_OPERATEURS);
        } else {
            bouton.setBackground(COULEUR_SCIENTIFIQUE);
        }

        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (texte.matches("[÷×\\-+=]")) {
                    bouton.setBackground(COULEUR_SURVOL_OPERATEURS);
                } else {
                    bouton.setBackground(COULEUR_SURVOL_BOUTONS);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (texte.matches("[0-9.]")) {
                    bouton.setBackground(COULEUR_BOUTONS);
                } else if (texte.matches("[÷×\\-+=]")) {
                    bouton.setBackground(COULEUR_OPERATEURS);
                } else {
                    bouton.setBackground(COULEUR_SCIENTIFIQUE);
                }
            }
        });

        bouton.addActionListener(e -> traiterTouche(texte));
        return bouton;
    }

    private void handleMemoryOperation(String operation) {
        try {
            switch (operation) {
                case "MC" -> memoryValue = 0;
                case "MR" -> setResultat(memoryValue);
                case "M+" -> memoryValue += Double.parseDouble(ecran.getText());
                case "M-" -> memoryValue -= Double.parseDouble(ecran.getText());
                case "MS" -> memoryValue = Double.parseDouble(ecran.getText());
            }
        } catch (Exception e) {
            ecran.setText("Erreur");
        }
    }

    private void basculerMode() {
        modeScientifique = !modeScientifique;
        if (modeScientifique) {
            setSize(LARGEUR_SCIENTIFIQUE, HAUTEUR_BASE);
            ajouterBoutonsScientifique();
        } else {
            setSize(LARGEUR_BASE, HAUTEUR_BASE);
            ajouterBoutonsStandard();
        }
        setLocationRelativeTo(null);
        getJMenuBar().getMenu(0).getItem(0).setText(modeScientifique ? "Standard" : "Scientifique");
        revalidate();
        repaint();
    }

    private void traiterTouche(String touche) {
        try {
            if (touche.matches("[0-9.]")) {
                if (debut) {
                    ecran.setText(touche);
                    debut = false;
                } else {
                    ecran.setText(ecran.getText() + touche);
                }
            } else {
                switch (touche) {
                    case "=" -> calculer();
                    case "C", "CE" -> {
                        ecran.setText("0");
                        nombre1 = 0;
                        operation = "";
                        debut = true;
                    }
                    case "⌫" -> {
                        String texte = ecran.getText();
                        if (texte.length() > 1) {
                            ecran.setText(texte.substring(0, texte.length() - 1));
                        } else {
                            ecran.setText("0");
                            debut = true;
                        }
                    }
                    case "±" -> {
                        double val = Double.parseDouble(ecran.getText());
                        ecran.setText(String.valueOf(-val));
                    }
                    case "√" -> {
                        double val = Double.parseDouble(ecran.getText());
                        if (val >= 0) {
                            setResultat(Math.sqrt(val));
                        } else {
                            ecran.setText("Nombre invalide");
                        }
                    }
                    case "x²" -> {
                        double val = Double.parseDouble(ecran.getText());
                        setResultat(val * val);
                    }
                    case "1/x" -> {
                        double val = Double.parseDouble(ecran.getText());
                        if (val != 0) {
                            setResultat(1.0 / val);
                        } else {
                            ecran.setText("Division par zéro");
                        }
                    }
                    case "%" -> {
                        if (!operation.isEmpty()) {
                            double val = Double.parseDouble(ecran.getText());
                            setResultat((nombre1 * val) / 100);
                        } else {
                            double val = Double.parseDouble(ecran.getText());
                            setResultat(val / 100);
                        }
                    }
                    case "sin" -> calculerTrigonometrie(Math::sin);
                    case "cos" -> calculerTrigonometrie(Math::cos);
                    case "tan" -> calculerTrigonometrie(Math::tan);
                    case "log" -> {
                        double val = Double.parseDouble(ecran.getText());
                        if (val > 0) {
                            setResultat(Math.log10(val));
                        } else {
                            ecran.setText("Nombre invalide");
                        }
                    }
                    case "ln" -> {
                        double val = Double.parseDouble(ecran.getText());
                        if (val > 0) {
                            setResultat(Math.log(val));
                        } else {
                            ecran.setText("Nombre invalide");
                        }
                    }
                    case "exp" -> {
                        double val = Double.parseDouble(ecran.getText());
                        setResultat(Math.exp(val));
                    }
                    case "MC", "MR", "M+", "M-", "MS" -> handleMemoryOperation(touche);
                    default -> {
                        if (touche.matches("[÷×\\-+]")) {
                            nombre1 = Double.parseDouble(ecran.getText());
                            operation = touche;
                            debut = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            ecran.setText("Erreur");
            debut = true;
        }
    }

    private void calculerTrigonometrie(java.util.function.Function<Double, Double> fonction) {
        try {
            double val = Double.parseDouble(ecran.getText());
            val = Math.toRadians(val);
            setResultat(fonction.apply(val));
        } catch (Exception e) {
            ecran.setText("Erreur");
        }
    }

    private void calculer() {
        try {
            double nombre2 = Double.parseDouble(ecran.getText());
            double resultat = switch (operation) {
                case "+" -> nombre1 + nombre2;
                case "-" -> nombre1 - nombre2;
                case "×" -> nombre1 * nombre2;
                case "÷" -> {
                    if (nombre2 == 0) throw new ArithmeticException("Division par zéro");
                    yield nombre1 / nombre2;
                }
                default -> nombre2;
            };
            setResultat(resultat);
        } catch (ArithmeticException e) {
            ecran.setText("Division par zéro");
        } catch (Exception e) {
            ecran.setText("Erreur");
        }
        debut = true;
    }

    private void setResultat(double resultat) {
        try {
            if (Double.isInfinite(resultat)) {
                ecran.setText("Infini");
            } else if (Double.isNaN(resultat)) {
                ecran.setText("Indéfini");
            } else {
                String formattedResult = df.format(resultat);
                if (formattedResult.length() > 15) {
                    ecran.setText("Dépassement");
                } else {
                    ecran.setText(formattedResult);
                }
            }
        } catch (Exception e) {
            ecran.setText("Erreur");
        }
        debut = true;
    }

    public static void main(String[] args) {
        try {
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      
            UIManager.put("Button.arc", 0);
            UIManager.put("TextField.arc", 0);

            SwingUtilities.invokeLater(() -> {
                Calculatrice calc = new Calculatrice();
                calc.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}   
