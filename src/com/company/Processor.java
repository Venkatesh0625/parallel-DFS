package com.company;

import java.util.Stack;

public class Processor extends Thread {
    private final int threadNumber;
    private final Graph graph;
    public Processor(Graph g,int id) {
        this.threadNumber = id;
        setName("Processor "+id);
        graph = g;
    }

    @Override
    public long getId() {
        return threadNumber;
    }

    @Override
    public void run() {
        while(!graph.isDone()){
            graph.dfs();
            Thread.yield();
            subDfs(graph.getLocalStacks().get(threadNumber));
        }
    }

    public void subDfs(Stack<Integer> localStack) {
        Stack<Integer> tmpStack = new Stack<>();
        while(!localStack.isEmpty()){
            int node = localStack.pop();
            if(!graph.getVisited(node)){
                graph.setVisited(node,true);
                graph.incrementCounter();
                boolean toLocal = true;
                for(int i = 0; i<graph.getSize(); i++){
                    if(node==i)continue;
                    if(graph.isNeighbour(node, i) && !toLocal && !graph.getVisited(i)) {
                        tmpStack.push(i);
                    }
                    if(graph.isNeighbour(node, i) && toLocal && !graph.getVisited(i)){
                        localStack.push(i);
                        toLocal = false;
                    }
                }
            }
        }
        graph.pushStack(tmpStack);
    }
}
