	AREA DATA1, DATA
U_ADD EQU 0x10000000
D_ADD EQU 0x1000001A
	AREA MYCODE, CODE
		ENTRY
		EXPORT __main2
			
			
__main2
	MOV R1, #0Xff
	CMP R1, #0Xff
	BEQ ubcd
	CMP R1, #0X00
	BEQ dbcd
	B STOP
ubcd
	LDR R2, =U_ADD
	MOV R3, #0X00
	STR R3, [R2]
	MOV R4, #0X01
	STR R4, [R2,#1]
	MOV R5, #0X02
	STR R5, [R2,#2]
	MOV R6, #0X03
	STR R6, [R2,#3]
	MOV R7, #0X04
	STR R7, [R2,#4]
	MOV R8, #0X05
	STR R8, [R2,#5]
	MOV R9, #0X06
	STR R9, [R2,#6]
	MOV R10, #0X07
	STR R10, [R2,#7]
	MOV R11, #0X08
	STR R11, [R2,#8]
	MOV R12, #0X09
	STR R12, [R2,#9]
	B STOP
dbcd
	LDR R2, =D_ADD
	MOV R3, #0X09 
	STR R3, [R2]
	MOV R4, #0X08 
	STR R4, [R2,#1]
	MOV R5, #0X07
	STR R5, [R2,#2]
	MOV R6, #0X06
	STR R6, [R2,#3]
	MOV R7, #0X05
	STR R7, [R2,#4]
	MOV R8, #0X04
	STR R8, [R2,#5]
	MOV R9, #0X03
	STR R9, [R2,#6]
	MOV R10, #0X02
	STR R10, [R2,#7]
	MOV R11, #0X01
	STR R11, [R2,#8]
	MOV R12, #0X00
	STR R12, [R2,#9]
	B STOP
STOP
	END
