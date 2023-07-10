import random
import requests
import locale
from datetime import datetime, timedelta
import concurrent.futures


# Função para gerar uma data aleatória entre duas datas
# Função para gerar uma data aleatória entre duas datas, garantindo que a data de início seja menor que a data fim
def random_date(start_date, end_date):
    while True:
        random_days = random.randint(0, (end_date - start_date).days)
        random_date = start_date + timedelta(days=random_days)
        if random_date < end_date:
            return random_date


# URL da API da aplicação
url = "http://192.168.49.2:32492/debts"

# Dados de exemplo
start_date = datetime(datetime.now().year, 1, 1)
end_date = datetime(datetime.now().year, 12, 31)
locale.setlocale(locale.LC_ALL, 'en_US.utf8')


def create_debt():
    value = locale.currency(round(random.uniform(1, 10000), 2), grouping=False, symbol=False)
    date = random_date(start_date, end_date)
    debt = {
        'amount': value,
        'startDate': date.strftime('%d/%m/%Y'),
        'endDate': date.strftime('%d/%m/%Y')
    }
    response = requests.post(url, json=debt)
    print(f"Debt created: {response.json()}")


# Criação das 50 dívidas
num_threads = 100

# Cria um executor para processar as requisições em paralelo
with concurrent.futures.ThreadPoolExecutor(max_workers=num_threads) as executor:
    # Mapeia a função de atualização de status para cada dívida vencida
    futures = [executor.submit(create_debt) for _ in range(20000)]

    # Aguarda a conclusão de todas as threads
    concurrent.futures.wait(futures)

print("Todas as dívidas vencidas foram processadas")
