package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

const (
	TYPE_SPACE = iota
	TYPE_NUMBER
	TYPE_SYMBOL
	TYPE_OTHERS         = -1
	SYMBOL_CODES string = "+-*/()"
)

type word struct {
	Type  int
	Value string
}

func lexer_analyze(line_sub []rune) word {
	for index := 0; index < len(line_sub); index++ {
		r := line_sub[index]
		if r == rune(' ') || r == rune('\t') {
			continue
		}
		if index > 0 {
			w := word{
				Type:  TYPE_SPACE,
				Value: string(line_sub[:index]),
			}
			return w
		}
		break
	}
	for index := 0; index < len(line_sub); index++ {
		r := line_sub[index]
		if r >= rune('0') && r <= rune('9') {
			continue
		}
		if index > 0 {
			w := word{
				Type:  TYPE_NUMBER,
				Value: string(line_sub[:index]),
			}
			return w
		}
		break
	}
	if strings.ContainsRune(SYMBOL_CODES, line_sub[0]) {
		w := word{
			Type:  TYPE_SYMBOL,
			Value: string(line_sub[0]),
		}
		return w
	}
	w := word{
		Type:  TYPE_OTHERS,
		Value: string(line_sub[0]),
	}
	return w
}

func lexer(line_org string) []*word {
	line := []rune(line_org)
	sentence := []*word{}
	for index := 0; index < len(line); {
		w := lexer_analyze(line[index:])
		if w.Type != TYPE_SPACE {
			sentence = append(sentence, &w)
		}
		r := []rune(w.Value)
		if len(r) == 0 {
			fmt.Println("Error: lexer analyze detected the 0 length word.")
			return nil
		}
		index += len(r)
	}
	return sentence
}

/*
	S := TERM | TERM + S | TERM - S
	TERM := PRODUCT | PRODUCT * TERM | PRODUCT / TERM
	PRODUCT := NUMBER | ( S )
*/

type syntax struct {
	Leaf  *word
	Child []*syntax
}

func parser_product(sentence []*word) (*syntax, int) {
	s := sentence[0]
	if s.Type == TYPE_NUMBER {
		sytx := syntax{
			Leaf:  s,
			Child: nil,
		}
		return &sytx, 1
	}
	if s.Type == TYPE_SYMBOL && s.Value == "(" {
		sytx, length := parser(sentence[1:])
		end := sentence[length+1]
		if end.Type == TYPE_SYMBOL && end.Value == ")" {
			return sytx, length + 2
		}
	}
	fmt.Println(s.Value)
	panic("not number")
}

func parser_term(sentence []*word) (*syntax, int) {
	s1, length1 := parser_product(sentence)
	if length1 >= len(sentence) {
		return s1, length1
	}
	s := sentence[length1]
	if s.Type == TYPE_SYMBOL &&
		(s.Value == "*" || s.Value == "/") {
		s2, length2 := parser_term(sentence[length1+1:])
		sytx := syntax{
			Leaf:  s,
			Child: []*syntax{s1, s2},
		}
		return &sytx, length1 + 1 + length2
	}
	return s1, length1
}

func parser(sentence []*word) (*syntax, int) {
	if sentence == nil || len(sentence) == 0 {
		return nil, 0
	}
	s1, length1 := parser_term(sentence)
	if length1 >= len(sentence) {
		return s1, length1
	}
	s := sentence[length1]
	if s.Type == TYPE_SYMBOL &&
		(s.Value == "+" || s.Value == "-") {
		s2, length2 := parser(sentence[length1+1:])
		sytx := syntax{
			Leaf:  s,
			Child: []*syntax{s1, s2},
		}
		return &sytx, length1 + 1 + length2
	}
	return s1, length1
}

func calc(sytx *syntax) int {
	leaf := sytx.Leaf
	if leaf.Type == TYPE_NUMBER {
		value, err := strconv.Atoi(leaf.Value)
		if err == nil {
			return value
		}
	}
	if leaf.Type == TYPE_SYMBOL {
		switch leaf.Value {
		case "+":
			return calc(sytx.Child[0]) + calc(sytx.Child[1])
		case "-":
			return calc(sytx.Child[0]) - calc(sytx.Child[1])
		case "*":
			return calc(sytx.Child[0]) * calc(sytx.Child[1])
		case "/":
			return calc(sytx.Child[0]) / calc(sytx.Child[1])
		}
	}
	panic("something wrong")
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()
		fmt.Println(line)
		sentence := lexer(line + "\n")
		if sentence == nil {
			return
		}
		if len(sentence) == 1 && sentence[0].Type == TYPE_OTHERS {
			continue
		}
		sytx, length := parser(sentence)
		if length != len(sentence)-1 {
			fmt.Println("Error")
			continue
		}
		result := calc(sytx)
		fmt.Println(result)
	}
	if err := scanner.Err(); err != nil {
		fmt.Fprintln(os.Stderr, "Error:", err)
	}
}
