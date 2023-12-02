package GradeHunter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 게임 랭킹 전체를 담당하는 클래스입니다.
 */
public class RankPanel extends JPanel {

    private Image backgroundImage;
    private MainPanel mainPanel;

    public RankPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(null); // 널 레이아웃 사용
        setBackground(Color.BLACK); // 배경색 설정

        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.getImage("GradeHunter/images/Gradcap.png");
        mainPanel.setIconImage(img);

        // 배경 이미지 로드
        backgroundImage = new ImageIcon("GradeHunter/images/bg_rank.png").getImage();
        setBounds(0, 0, mainPanel.getWidth(), mainPanel.getHeight());

        ImageIcon imgIcon = new ImageIcon("GradeHunter/images/bt_home.png");
        JButton button = new JButton(imgIcon);
        button.setBounds(40, 35, imgIcon.getIconWidth(), imgIcon.getIconHeight());
        add(button);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);



        ImageIcon imgIconEntered = new ImageIcon("GradeHunter/images/bt_home_entered.png");

        // 버튼 위에 마우스가 올라갔을 때 커서를 손가락 모양으로 변경
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));


        // 마우스 호버 시 버튼 이미지 변경
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(imgIconEntered);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(imgIcon);
            }
        });

        button.addActionListener(e -> {
            // MainPanel에서 값 초기화 후 전환
            this.mainPanel.resetValues();
            // MainPanel의 메인 콘텐츠 패널로 돌아가기
            this.mainPanel.switchPanel(this.mainPanel.getMainContentPanel());
        });



        SortedDataFileReader fileReader = new SortedDataFileReader();
        List<Record> records = fileReader.readFileAndSort("GradeHunter/data.txt");

        // 랭킹 데이터 표시
        int yPosition = 60; // 랭킹 시작 위치 조정
        for (int i = 0; i < Math.min(records.size(), 10); i++) {
            JLabel rankingLabel = new JLabel("            " + records.get(i).toString(), SwingConstants.LEFT);
            rankingLabel.setForeground(Color.WHITE);
            rankingLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 23));
            rankingLabel.setBounds(130, yPosition, 1000, 378); // y
            yPosition += 34; // 다음 레코드의 y 위치 증가
            add(rankingLabel);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private class Record {
        String id;
        String time;
        String subject;

        public Record(String id, String time, String subject) {
            this.id = id;
            this.time = time;
            this.subject = subject;
        }

        @Override
        public String toString() {
            return id + "                       " + time + "                           " + subject;
        }
    }

    private class SortedDataFileReader {
        public List<Record> readFileAndSort(String filePath) {
            List<Record> records = new ArrayList<>();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" "); // 공백을 기준으로 데이터를 분리
                    if (parts.length >= 3) {
                        String id = parts[0];
                        String time = parts[1];
                        String subject = parts[2];
                        records.add(new Record(id, time, subject));
                    }
                }

                Collections.sort(records, Comparator.comparing(r -> r.time)); // 시간을 기준으로 정렬

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return records;
        }
    }

}