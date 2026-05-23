package me.qigan.zacoxlo.pre;

import javax.swing.*;
import java.awt.*;

public class PreProgressionWindow extends JFrame {

    public static final Font M_font = new Font(Font.MONOSPACED, Font.PLAIN, 24);

    public final JLabel WebICEF_prog_bar;

    public PreProgressionWindow() {
        super("Zacoxlo pre loading...");
        this.setSize(650, 500);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.WebICEF_prog_bar = new JLabel("WebI | JCEF load");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.WebICEF_prog_bar.setFont(M_font);

        this.add(this.WebICEF_prog_bar);
    }
}
