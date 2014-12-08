//Ken Devane
//CS 211 Fall '14 Final Exam
import java.io.*;
import java.util.*;

public class Exam2{
	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);
		if(args.length!=0){
			if(args.length>1){
				System.out.println("More than one file was entered! Quitting...");
				return;
			}
			String inputFileName="*";
			for(int x=0;x<args.length;x++){
				if(args[x].contains(".txt")){
					inputFileName=args[x];
					break;
				}
			}

			try{
				sc=new Scanner(new File(inputFileName));
				System.out.println("Opening file " + inputFileName);
			}
			catch(IOException ioe){
				System.out.println("File does not exist! Quitting...");
				return;
			}
		}

		DStack stack1=new DStack();
		DStack stack2=new DStack();

		System.out.println("Enter values, ending input with a negative value: ");
		double value=0;
		value=sc.nextDouble();
		while(value>=0){
			while((!stack1.isEmpty()) && (stack1.top()<value)){
				stack2.push(stack1.top());
				stack1.pop();
			}
			stack1.push(value);
			while(!stack2.isEmpty()){
				stack1.push(stack2.top());
				stack2.pop();
			}
			value = sc.nextDouble();
		}


		int x=0;
		while(!stack1.isEmpty()){
			if(x%8==0)
				System.out.println();
			System.out.print(stack1.top()+"  ");
			stack1.pop();
			x++;
		}
		System.out.println();
	}

}

class DStack{

	private Node head;

	public DStack(){
		head=null;
	}

	public void push(double value){
		if(head==null){
			head=new Node(value);
		}
		else{
			Node newNode=new Node(value);
			newNode.setNext(head);
			head=newNode;
		}
	}

	public void pop(){
		if(head==null){
			return;
	}
		head=head.getNext();
	}


	public double top(){
		return head.getData();
	}

	public boolean isEmpty(){
		if(head==null){
			return true;
		}
		else
			return false;
	}
}


class Node{

	private Node next;
	private double data;

	public Node(double data){
		this.data=data;
		this.next=null;
	}

	public Node getNext(){
		return next;
	}

	public double getData(){
		return data;
	}

	public void setNext(Node next){
		this.next=next;
	}
}