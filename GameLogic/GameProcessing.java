package GameLogic;
import java.util.Random;
import java.util.Vector;

public class GameProcessing{
    public final char[][] board;
    public int WScore, BScore, remaining;

    public GameProcessing(){
        board = new char[][]{
                {'-','-','-','-','-','-','-','-',},
                {'-','-','-','-','-','-','-','-',},
                {'-','-','-','-','-','-','-','-',},
                {'-','-','-','W','B','-','-','-',},
                {'-','-','-','B','W','-','-','-',},
                {'-','-','-','-','-','-','-','-',},
                {'-','-','-','-','-','-','-','-',},
                {'-','-','-','-','-','-','-','-',},
        };
    }

    public class Point{
        public int x, y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o){
            return o.hashCode()==this.hashCode();
            //return (o.x==this.x && o.y==this.y)
        }

        @Override
        public int hashCode()
        {
            return Integer.parseInt(x+""+y);
        }
    }

    public Vector<Point> find_valid_moves(char player, char opponent, int display){
        Vector<Point> valid_moves = new Vector<Point>();
        for(int i=0;i<8;++i){
            for(int j=0;j<8;++j){
                if(board[i][j] == player){
                    int p=i, q=j; //diagonally above on left
                    if(p-1>=0 && q-1>=0 && board[p-1][q-1]==opponent){
                        --p; --q;
                        while(p>0 && q>0 && board[p][q]==opponent){ --p; --q; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }
                    p=i;q=j; //above
                    if(p-1>=0 && board[p-1][q]==opponent){
                        --p;
                        while(p>0 && board[p][q]==opponent) { --p; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }
                    p=i;q=j; //diagonally above on right
                    if(p-1>=0 && q+1<=7 && board[p-1][q+1]==opponent){
                        --p;++q;
                        while(p>0 && q<7 && board[p][q]==opponent) { --p; ++q; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }
                    p=i;q=j; //right
                    if(q+1<=7 && board[p][q+1]==opponent){
                        ++q;
                        while(q<7 && board[p][q]==opponent) { ++q; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }
                    p=i;q=j; //diagonally below on right
                    if(p+1<=7 && q+1<=7 && board[p+1][q+1]==opponent){
                        ++p; ++q;
                        while(p<7 && q<7 && board[p][q]==opponent) { ++p; ++q; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }
                    p=i;q=j; //below
                    if(p+1<=7 && board[p+1][q]==opponent){
                        ++p;
                        while(p<7 && board[p][q]==opponent) { ++p; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }
                    p=i;q=j; //diagonally below on left
                    if(p+1<=7 && q-1>=0 && board[p+1][q-1]==opponent){
                        ++p; --q;
                        while(p<7 && q>0 && board[p][q]==opponent) { ++p; --q; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }
                    p=i;q=j; //left
                    if(q-1>=0 && board[p][q-1]==opponent){
                        --q;
                        while(q>0 && board[p][q]==opponent) { --q; }
                        if(board[p][q]=='-'){
                            valid_moves.add(new Point(p, q)); board[p][q]='@';
                        }
                    }

                }
            }
        }

        if(display==1)
            display_board(this);
        for(Point p:valid_moves)
            board[p.x][p.y]='-';   //restore '@' to '-'
        return valid_moves;
    }

    public void display_board(GameProcessing b){
        for(int i=0;i<9;++i)
            System.out.print(" ___");
        System.out.println();
        //System.out.println("_");

        System.out.print("|   |");
        for(int i=0;i<8;++i)
            System.out.print(" "+(i+1)+" |");
        System.out.println();

        for(int i=0;i<9;++i)
            System.out.print("|___");
        System.out.println("|");

        for(int i=0;i<8;++i){
            System.out.print("| "+(i+1)+" ");
            for(int j=0;j<8;++j)
                System.out.print("| "+b.board[i][j]+" ");
            System.out.println("|");
            for(int k=0;k<9;++k)
                System.out.print("|___");
            System.out.println("|");
        }
        System.out.println();
    }


    public int game_result(Vector<Point> white_valid_moves, Vector<Point> black_valid_moves){
        update_scores();
        if(remaining == 0){
            if(WScore > BScore) return 1;
            else if(BScore > WScore) return -1;
            else return 0; //tie
        }
        if(WScore==0 || BScore == 0){
            if(WScore > 0) return 1;
            else return -1;
        }
        if(white_valid_moves.isEmpty() && white_valid_moves.isEmpty()){
            if(WScore > BScore) return 1;
            else if(BScore > WScore) return -1;
            else return 0; //tie
        }
        return -2;
    }

    public int place_move(Point p, char player, char opponent){
        int m = p.x, n = p.y;
        board[m][n] = player;

        int i=m,j=n;   //diagonally above on left
        if(i-1>=0 && j-1>=0 && board[i-1][j-1] == opponent){
            --i; --j;
            while(i>0 && j>0 && board[i][j] == opponent){ --i ; --j; }
            if(board[i][j] == player){
                while(i!=m-1 && j!=n-1) board[++i][++j]=player;
            }
        }

        i=m;j=n;   //above
        if(i-1>=0 && board[i-1][j] == opponent){
            --i;
            while(i>0 && board[i][j] == opponent) { --i; }
            if(board[i][j] == player){
                while(i!=m-1) board[++i][j]=player;
            }
        }

        i=m;j=n;  //diagonally above on right
        if(i-1>=0 && j+1<=7 && board[i-1][j+1] == opponent){
            --i; ++j;
            while(i>0 && j<7 && board[i][j] == opponent) { --i; ++j; }
            if(board[i][j] == player){
                while(i!=m-1 && j!=n+1) board[++i][--j] = player;
            }
        }

        i=m;j=n;  //right
        if(j+1<=7 && board[i][j+1] == opponent){
            ++j;
            while(j<7 && board[i][j] == opponent) { j++; }
            if(board[i][j] == player){
                while(j!=n+1) board[i][--j] = player;
            }
        }

        i=m;j=n; //diagonally below on right
        if(i+1<= 7 && j+1<=7 && board[i+1][j+1] == opponent){
            ++i; ++j;
            while(i<7 && j<7 && board[i][j] == opponent) { ++i; ++j; }
            if(board[i][j] == player){
                while(i!=m+1 && j!=n+1) board[--i][--j] = player;
            }
        }

        i=m;j=n;  //below
        if(i+1<= 7 && board[i+1][j] == opponent){
            ++i;
            while(i<7 && board[i][j] == opponent) { i++; }
            if(board[i][j] == player){
                while(i!=m+1) board[--i][j] = player;
            }
        }

        i=m;j=n;  //diagonally below on left
        if(i+1<=7 && j-1>=0 && board[i+1][j-1] == opponent){
            ++i;--j;
            while(i<7 && j>0 && board[i][j] == opponent) { ++i; --j;}
            if(board[i][j] == player){
                while(i!=m+1 && j!=n-1) board[--i][++j] = player;
            }
        }

        i=m;j=n;  //left
        if(j-1>=0 && board[i][j-1] == opponent){
            --j;
            while(j>0 && board[i][j] == opponent) { --j; }
            if(board[i][j] == player){
                while(j!=n-1) board[i][++j] = player;
            }
        }
        return 0;
    }
    public int place_move_modified(Point p, char player, char opponent){
        int m = p.x, n = p.y,count=0;
        board[m][n] = player;

        int i=m,j=n;   //diagonally above on left
        if(i-1>=0 && j-1>=0 && board[i-1][j-1] == opponent)
        {
            --i; --j;
            while(i>0 && j>0 && board[i][j] == opponent){ --i ; --j; }
            if(board[i][j] == player){
                while(i!=m-1 && j!=n-1)
                {
                    board[++i][++j]=player;
                    count++;
                }
                //board[++i][++j]=player;
            }
        }

        i=m;j=n;   //above
        if(i-1>=0 && board[i-1][j] == opponent){
            --i;
            while(i>0 && board[i][j] == opponent) { --i; }
            if(board[i][j] == player){
                while(i!=m-1)
                {
                    board[++i][j]=player;
                    count++;
                }
            }
        }

        i=m;j=n;  //diagonally above on right
        if(i-1>=0 && j+1<=7 && board[i-1][j+1] == opponent){
            --i; ++j;
            while(i>0 && j<7 && board[i][j] == opponent) { --i; ++j; }
            if(board[i][j] == player){
                while(i!=m-1 && j!=n+1)

                {
                    board[++i][--j] = player;
                    count++;
                }
            }
        }

        i=m;j=n;  //right
        if(j+1<=7 && board[i][j+1] == opponent){
            ++j;
            while(j<7 && board[i][j] == opponent) { j++; }
            if(board[i][j] == player){
                while(j!=n+1) {
                    board[i][--j] = player;
                    count++;}
            }
        }

        i=m;j=n; //diagonally below on right
        if(i+1<= 7 && j+1<=7 && board[i+1][j+1] == opponent){
            ++i; ++j;
            while(i<7 && j<7 && board[i][j] == opponent) { ++i; ++j; }
            if(board[i][j] == player){
                while(i!=m+1 && j!=n+1) {board[--i][--j] = player;
                    count++;}
            }
        }

        i=m;j=n;  //below
        if(i+1<= 7 && board[i+1][j] == opponent){
            ++i;
            while(i<7 && board[i][j] == opponent) { i++; }
            if(board[i][j] == player){
                while(i!=m+1) {board[--i][j] = player;
                    count++;}
            }
        }

        i=m;j=n;  //diagonally below on left
        if(i+1<=7 && j-1>=0 && board[i+1][j-1] == opponent){
            ++i;--j;
            while(i<7 && j>0 && board[i][j] == opponent) { ++i; --j;}
            if(board[i][j] == player){
                while(i!=m+1 && j!=n-1) {board[--i][++j] = player;
                    count++;}
            }
        }

        i=m;j=n;  //left
        if(j-1>=0 && board[i][j-1] == opponent){
            --j;
            while(j>0 && board[i][j] == opponent) { --j; }
            if(board[i][j] == player){
                while(j!=n-1) {board[i][++j] = player;
                    count++;}
            }
        }
        return count;
    }
    public void update_scores(){
        WScore = 0; BScore = 0; remaining = 0;
        for(int i=0;i<8;++i){
            for(int j=0;j<8;++j){
                if(board[i][j]=='W') ++WScore;
                else if(board[i][j]=='B') ++BScore;
                else ++remaining;
            }
        }
    }

    public Point computer_move(Vector<Point> valid_moves){
        Random r = new Random();
        int max = valid_moves.size();
        int v_index = r.nextInt(max);

        Point p = new Point(0,0);
        p=valid_moves.get(v_index);
        int i=p.x;
        int j=p.y;
        return p;
    }
}