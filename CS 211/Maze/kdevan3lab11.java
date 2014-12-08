import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class kdevan3lab11{
	public static void main(String[] args){

		//Looks for the command line argument containing
		//the name of the data text file
		String inputFileName="*";
		for (int i=0; i<args.length; i++){
			if(args[i].contains(".txt")){
			 	inputFileName=args[i];
			 	break;
			}
		}
		if(inputFileName.equals("*")){
			System.out.println("Error! File does not exist, or a data file was not specified!!");
			System.out.println("Usage: [java] [kdevan3lab11] [\"filename.txt\"]");
			return;
		}


		//String inputFileName="mazeData1.txt";
		MazeData m1=new MazeData(inputFileName);
		boolean flag=m1.readInData();
		if(flag){
			System.out.println("Quitting...");
			return;
		}

		//gets the starting point and initializes
		//xpos and ypos for later use
		int[][] startPoint=m1.getStartPoint();
		int xpos=startPoint[0][0];
		int ypos=startPoint[0][1];
		int xstart=xpos;
		int ystart=ypos;

		//gets the end point
		int[][] endPoint=m1.getEndPoint();
		int xend=endPoint[0][0];
		int yend=endPoint[0][1];

		//gets the size of the maze
		int[][] size=m1.getSize();
		int xsize=size[0][0];
		int ysize=size[0][1];

		//gets the maze itself
		char[][] maze=m1.getMaze();

		//Initialize a new GridDisplay to output the animation
		GridDisplay disp=new GridDisplay(xsize+2,ysize+2);
		for(int i=0; i<xsize+2; i++){
			for(int j=0; j<ysize+2; j++){
		      	disp.setChar(i,j,maze[i][j]);
		      	if(maze[i][j]=='*')
		      		disp.setColor(i,j,Color.blue);
			}
		}
		disp.setColor(xpos,ypos,Color.red);
		disp.setColor(xend,yend,Color.red);
		GridDisplay.mySleep(200);

		System.out.println(m1+"\n");

		//Creates a new stack
		Stack stack=new Stack();

		//Push the starting coordinates onto the stack
		stack.push(xpos,ypos);

		//temporary 2-D array to store returned coordinates
		int temp[][];

		do{

			//Make sure the start point is always color red
			if(xpos==xstart&&ypos==ystart){
				disp.setChar(xpos,ypos,'s');
				disp.setColor(xpos,ypos,Color.red);
				GridDisplay.mySleep(100);
			}
			temp=stack.getLastCoordinates();
			xpos=temp[0][0];
			ypos=temp[0][1];

			//Checks if a solution has been found
			if(xpos==xend&&ypos==yend){
				System.out.println("The end has been found! The path is:");
				break;
			}

			//Marks visited positions with a 'v' and colors it green
			if((m1.notVisited(xpos,ypos))&&(m1.notABlock(xpos,ypos))){
				m1.setVisited(xpos,ypos);
				disp.setChar(xpos,ypos,'v');
				disp.setColor(xpos,ypos,Color.green);
				GridDisplay.mySleep(100);
			}

			//Checks all four adjacent spots for a valid move
			if((m1.notVisited(xpos+1,ypos))&&(m1.notABlock(xpos+1,ypos)))
				stack.push(xpos+1,ypos);

			else if((m1.notVisited(xpos,ypos+1))&&(m1.notABlock(xpos,ypos+1)))
				stack.push(xpos,ypos+1);

			else if((m1.notVisited(xpos-1,ypos))&&(m1.notABlock(xpos-1,ypos)))
				stack.push(xpos-1,ypos);

			else if((m1.notVisited(xpos,ypos-1))&&(m1.notABlock(xpos,ypos-1)))
				stack.push(xpos,ypos-1);

			//All moves are not valid, so pop the top item on the stack or exit
			else{
				temp=stack.pop();
				if(stack.getStackSize()!=0){
					xpos=temp[0][0];
					ypos=temp[0][1];
					disp.setChar(xpos,ypos,'v');
					disp.setColor(xpos,ypos,Color.lightGray);
					GridDisplay.mySleep(100);
				}
				else{
					System.out.println("There are no more possible moves, and so there is no solution.");
					break;
				}
			}

			//Stack is empty, so there is no solution
			if(stack.getStackSize()==0){
				System.out.println("There are no more possible moves, and so there is no solution.");
				break;
			}

    	}while(stack.getStackSize()!=0);

		//m1.displayMaze();

		//Prints out the solution to the maze
		stack.displayStack();

	}
}//End class kdevan3lab11


//Class for the stack array that holds visited positions
class Stack{
	private int[][] stack;
	private int stackSize;

	public Stack(){
		stackSize=0;
	}

	public int getStackSize(){
		return stackSize;
	}

	public int[][] getLastCoordinates(){
		int[][] tempStack=new int[1][2];
		tempStack[0][0]=stack[stackSize-1][0];
		tempStack[0][1]=stack[stackSize-1][1];
		return tempStack;
	}

	public void push(int xpos,int ypos){
		int[][] tempStack=new int[stackSize+1][2];
		for(int x=0;x<stackSize;x++){
			tempStack[x][0]=stack[x][0];
			tempStack[x][1]=stack[x][1];
		}
		tempStack[stackSize][0]=xpos;
		tempStack[stackSize][1]=ypos;
		stack=tempStack;
		stackSize++;
	}

	public int[][] pop(){
		int[][] poppedCoordinates=new int[1][2];
		if(stackSize==0){
			poppedCoordinates[0][0]=-1;
			poppedCoordinates[0][1]=-1;
			return poppedCoordinates;
		}
		poppedCoordinates[0][0]=stack[stackSize-1][0];
		poppedCoordinates[0][1]=stack[stackSize-1][1];
		int[][] tempStack=new int[stackSize][2];
		for(int x=0;x<stackSize-1;x++){
			tempStack[x][0]=stack[x][0];
			tempStack[x][1]=stack[x][1];
		}
		stack=tempStack;
		stackSize--;
		return poppedCoordinates;
	}

	public void displayStack(){
		for(int x=0;x<stackSize;x++){
			if(x%6==0&&x!=0)
				System.out.println();
			if(x==stackSize-1)
				System.out.println("("+stack[x][0]+","+stack[x][1]+")");
			else
				System.out.print("("+stack[x][0]+","+stack[x][1]+") -> ");
		}
	}

	public String toString(){
		System.out.println("Stack is:");
		displayStack();
		return "Stack size is "+stackSize+".";

	}
}//End class Stack



//Class to hold all the information concerning the maze
class MazeData{

 	private char[][] array;
	private int xsize,ysize;
	private int xstart,ystart;
	private int xend,yend;
	private String inputFileName;

	public MazeData(String fileName){
		inputFileName=fileName;
	}

	public char[][] getMaze(){
		return array;
	}

	public int[][] getStartPoint(){
		int[][] point=new int[1][2];
		point[0][0]=xstart;
		point[0][1]=ystart;
		return point;
	}

	public int[][] getSize(){
		int[][] point=new int[1][2];
		point[0][0]=xsize;
		point[0][1]=ysize;
		return point;
	}

	public int[][] getEndPoint(){
		int[][] point=new int[1][2];
		point[0][0]=xend;
		point[0][1]=yend;
		return point;
	}

	public boolean readInData(){
		File f=new File(inputFileName);
		Scanner sc=null;

	   	try{
			sc=new Scanner(f);
	   	}
		catch (FileNotFoundException fnfe){
			System.out.println("File does not exist!");
			return true;
	  	}

		//Read in the size, start, and end of the maze
		xsize=sc.nextInt();
		ysize=sc.nextInt();
		xstart=sc.nextInt();
		ystart=sc.nextInt();
		xend=sc.nextInt();
		yend=sc.nextInt();

		//Check for errors in maze size
		if(xsize<1){
			System.out.println("The X dimension "+xsize+" is too small!");
		    return true;
		}
		if(ysize<1){
			System.out.println("The Y dimension "+ysize+" is too small!");
		    return true;
  		}

  		//Check for errors in start and end points
  		if(xstart<=0||ystart<=0||xstart>xsize||ystart>ysize){
			System.out.println("The starting point is invalid!");
			return true;
		}
		if(xend<=0||yend<=0||xend>xsize||yend>ysize){
			System.out.println("The ending point is invalid!");
			return true;
		}

		//Create and intialize the maze array
  		array=new char[xsize+2][ysize+2];
  		for(int i=0; i<xsize+2; i++){
			for(int j=0; j<ysize+2; j++)
      			array[i][j]='.';
		}
		for(int i=0; i<xsize+2; i++){
			array[i][0]='*';
			array[i][ysize+1]='*';
		}
		for(int i=0; i<ysize+2; i++){
			array[0][i]='*';
			array[xsize+1][i]='*';
		}

		//Mark the starting and ending positions in the maze
		array[xstart][ystart]='s';
  		array[xend][yend]='e';

		int count=0;
		int xpos=0;
		int ypos=0;
		while(sc.hasNextInt()){
			xpos=sc.nextInt();
			ypos=sc.nextInt();
			if(xpos>xsize||xpos<1)
		         System.out.println("There is an X outside the range!!");
			else if(xpos==xstart&&ypos==ystart)
		         System.out.println("Coordinate is on the starting point!!");
			else if(ypos>ysize||ypos<1)
		      	 System.out.println("There is a Y outside the range!!");
			else
			     array[xpos][ypos]='*';
			count++;
		}

		return false;
	}

	public void setVisited(int xpos,int ypos){
		array[xpos][ypos]='v';
	}

	public boolean notVisited(int xpos, int ypos){
		if(array[xpos][ypos]=='v')
			return false;
		else
			return true;
	}

	public boolean notABlock(int xpos,int ypos){
		if(array[xpos][ypos]=='*')
			return false;
		else
			return true;
	}

	public void displayMaze(){
		for(int i=0; i<xsize+2; i++){
			for(int j=0; j<ysize+2; j++){
				System.out.print(array[i][j]);
			}
			System.out.println();
		}
	}

	public String toString(){
		return "Input file name is "+inputFileName+
		". Maze size is "+xsize+" x "+ysize+
		".\nStarting position is ("+xstart+","+ystart+
		"). Ending position is ("+xend+","+yend+").";
	}

}//End class MazeData



class GridDisplay extends JFrame
{
   private JLabel labels[];

   private Container container;
   private GridLayout grid1;
   int rowCount;
   int colCount;

   // set up GUI
   public GridDisplay(int rows, int cols)
   {
      super("The Maze");
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

      // set up grid layout structure of the display
      rowCount = rows;
      colCount = cols;
      grid1 = new GridLayout( rows, cols );
      container = getContentPane();
      container.setLayout( grid1 );

      // create and add buttons
      labels = new JLabel[ rows * cols ];

      for ( int count = 0; count < labels.length; count++ ) {
         labels[ count ] = new JLabel( " " );
         labels[count].setOpaque(true);
         container.add( labels[ count ] );
      }

      // set up the size of the window and show it
      setSize( cols * 15 , rows * 15 );
      setVisible( true );

   } // end constructor GridLayoutDemo

   // display the given char in the (row,col) position of the display
   public void setChar (int row, int col, char c)
   {
     if ((row >= 0 && row < rowCount) && (col >= 0 && col < colCount) )
     {
       int pos = row * colCount + col;
       labels [pos].setText("" + c);
     }
   }

   // display the given color in the (row,col) position of the display
   public void setColor (int row, int col, Color c)
   {
     if ((row >= 0 && row < rowCount) && (col >= 0 && col < colCount) )
     {
       int pos = row * colCount + col;
       labels [pos].setBackground(c);
     }
   }

   public static void mySleep( int milliseconds)
       {
         try
         {
           Thread.sleep(milliseconds);
         }
         catch (InterruptedException ie)
         {
         }
    }
} // end class GridDisplay
