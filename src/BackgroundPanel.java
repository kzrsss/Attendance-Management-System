import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        // 使用类加载器加载图片
        setBackgroundImage("login.png"); // 图片在项目根目录下
    }

    public void setBackgroundImage(String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        backgroundImage = icon.getImage();
        repaint(); // 请求重新绘制面板
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        if (backgroundImage != null) {
            // 绘制缩放后的图片以填充整个面板
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // 确保面板的首选大小与窗口大小相匹配
        return new Dimension(800, 600);
    }
}