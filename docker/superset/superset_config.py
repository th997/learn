# upload
import os 
BASE_DIR = os.path.abspath(os.path.dirname(__file__))
UPLOAD_FOLDER = BASE_DIR + '/app/static/uploads/'
IMG_UPLOAD_FOLDER = BASE_DIR + '/app/static/uploads/'

# cache
CACHE_CONFIG = {
    'CACHE_TYPE': 'redis',
    'CACHE_DEFAULT_TIMEOUT': 60*10,
    'CACHE_KEY_PREFIX': 'superset',
    'CACHE_REDIS_URL': 'redis://redis:6379',
}
DATA_CACHE_CONFIG = CACHE_CONFIG

# mysql
SQLALCHEMY_DATABASE_URI = 'mysql://root:mysql_666888@10.10.10.106:53306/superset?charset=utf8mb4'

# encrypt
SECRET_KEY = 'awm;o4;865yqhalk.4257'

