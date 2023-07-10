import requests
import concurrent.futures

active = True
status = "Active" if active else "Overdue"


def update_debt_status(thread):
    url = "http://192.168.49.2:32492/debts/update-overdue-debts-status"
    response = requests.patch(url, dict(status=status, clientId=thread+1))
    if response.status_code == 200:
        print(f"Processou: {response.status_code} - {response.content}")
    return response.status_code


# Define o número de threads para executar em paralelo
num_threads = 200

# Cria um executor para processar as requisições em paralelo
with concurrent.futures.ThreadPoolExecutor(max_workers=num_threads) as executor:
    results = executor.map(update_debt_status, range(num_threads))

# Conta quantas threads receberam o status code 200 e 422
count_200 = 0
count_422 = 0

for result in results:
    if result == 200:
        count_200 += 1
    elif result == 422:
        count_422 += 1

print(f"Quantidade de retornos com status code 200: {count_200}")
print(f"Quantidade de retornos com status code 422: {count_422}")
