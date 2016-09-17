import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Random;
import java.applet.*;

/*
<applet code="TicTacToe" width=600 height=600>
</applet>
*/

public class TicTacToe extends JApplet 
{
  private Board board;
  private Color oColor=Color.BLUE, xColor=Color.RED;
  static final char BLANK=' ', O='O', X='X';
  private char position[]={  
    BLANK, BLANK, BLANK,
    BLANK, BLANK, BLANK,
    BLANK, BLANK, BLANK};
  private int wins=0, losses=0, draws=0;
  
  public void init() 
  {
    add(board=new Board(), BorderLayout.CENTER);
    setVisible(true);
  }
  
  private class Board extends JPanel implements MouseListener 
  {
    private Random random=new Random();
    private int rows[][]={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
 
    public Board() 
	{
      addMouseListener(this);
    }

	public void paintComponent(Graphics g) 
	{
      super.paintComponent(g);
      int w=getWidth();
      int h=getHeight();
      Graphics2D g2d = (Graphics2D) g;

      g2d.setPaint(Color.WHITE);
      g2d.fill(new Rectangle2D.Double(0, 0, w, h));
      g2d.setPaint(Color.BLACK);
      g2d.setStroke(new BasicStroke(5));
      g2d.draw(new Line2D.Double(0, h/3, w, h/3));
      g2d.draw(new Line2D.Double(0, h*2/3, w, h*2/3));
      g2d.draw(new Line2D.Double(w/3, 0, w/3, h));
      g2d.draw(new Line2D.Double(w*2/3, 0, w*2/3, h));
		
      for (int i=0; i<9; ++i) 
	  {
        double xpos=(i%3+0.5)*w/3.0;
        double ypos=(i/3+0.5)*h/3.0;
        double xr=w/8.0;
        double yr=h/8.0;
        if (position[i]==O) 
		{
          g2d.setPaint(oColor);
          g2d.draw(new Ellipse2D.Double(xpos-xr, ypos-yr, xr*2, yr*2));
        }
        else if (position[i]==X) 
		{
          g2d.setPaint(xColor);
          g2d.draw(new Line2D.Double(xpos-xr, ypos-yr, xpos+xr, ypos+yr));
          g2d.draw(new Line2D.Double(xpos-xr, ypos+yr, xpos+xr, ypos-yr));
        }
      }
    }

    public void mouseClicked(MouseEvent e) 
	{
      int xpos=e.getX()*3/getWidth();
      int ypos=e.getY()*3/getHeight();
      int pos=xpos+3*ypos;
      if (pos>=0 && pos<9 && position[pos]==BLANK) 
	  {
        position[pos]=O;
        repaint();
        putX();
        repaint();
      }
    }
	
    void putX() 
	{
	  if (won(O))
        newGame(O);
      else if (isDraw())
        newGame(BLANK);

      else 
	  {
        nextMove();
        if (won(X))
          newGame(X);
        else if (isDraw())
          newGame(BLANK);
      }
    }

	boolean won(char player) 
	{
      for (int i=0; i<8; ++i)
        if ((position[rows[i][0]]==player && position[rows[i][1]]==player && position[rows[i][2]]==player))
          return true;
      return false;
    }

    boolean isDraw() 
	{
      for (int i=0; i<9; ++i)
        if (position[i]==BLANK)
          return false;
      return true;
    }
	
    void nextMove() 
	{
      int r=findRow(X);  
      if (r<0)
        r=findRow(O);  
      if (r<0) 
	  {  
        do
          r=random.nextInt(9);
        while (position[r]!=BLANK);
      }
      position[r]=X;
    }

    int findRow(char player) 
	{
      for (int i=0; i<8; ++i) 
	  {
		if (position[rows[i][0]]==player && position[rows[i][1]]==player && position[rows[i][2]]==BLANK)
			return rows[i][2];
		if (position[rows[i][0]]==player && position[rows[i][2]]==player && position[rows[i][1]]==BLANK)
			return rows[i][1];
		if (position[rows[i][1]]==player && position[rows[i][2]]==player && position[rows[i][0]]==BLANK)
			return rows[i][0];
      }
      return -1;
    }

	void newGame(char winner) 
	{
      repaint();

      String result;
      if (winner==O) 
	  {
        ++wins;
        result = "You Win!";
      }
      else if (winner==X) 
	  {
        ++losses;
        result = "I Win!";
      }
      else 
	  {
        result = "Tie";
        ++draws;
      }
	  
      if (JOptionPane.showConfirmDialog(null, "You have "+wins+ " wins, "+losses+" losses, "+draws+" draws\n"+"Play again?", result, JOptionPane.YES_NO_OPTION) !=JOptionPane.YES_OPTION ) 
	  {
        wins=losses=draws=0;
      }

      for (int j=0; j<9; ++j)
        position[j]=BLANK;
	}

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

 }
}